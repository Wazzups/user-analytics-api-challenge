package com.wazzups.analytics.userapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wazzups.analytics.userapi.Exception.InvalidJsonException;
import com.wazzups.analytics.userapi.dto.TeamInsight;
import com.wazzups.analytics.userapi.model.LogEntry;
import com.wazzups.analytics.userapi.model.Project;
import com.wazzups.analytics.userapi.model.User;
import com.wazzups.analytics.userapi.repository.InMemoryUserRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserRepository repository;
    private final ObjectMapper mapper;

    public UserService(InMemoryUserRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void importUsers(List<User> users) {
        repository.saveAll(users);
    }

    public void importUsersFromJsonFile(MultipartFile file) {
        try(InputStream is = file.getInputStream()) {
            List<User> users = mapper.readValue(
                is,
                new TypeReference<List<User>>() {}
            );

            importUsers(users);
        } catch (IOException e) {
            throw new InvalidJsonException(e.getMessage(), e);
        }
    }

    public List<User> superUsers() {
        return repository.findAll().stream().filter(u -> u.getScore() >= 900 && u.isActive()).toList();
    }

    public Map<String, Long> topCountries(int topN) {
        return repository.findAll().stream().collect(Collectors.groupingBy(User::getCountry, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
            .limit(topN)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a,b)->a,
                LinkedHashMap::new
            ));
    }

    public List<TeamInsight> teamInsights() {
        List<User> users = repository.findAll();

        Map<String, List<User>> byTeam = users.stream().collect(Collectors.groupingBy(u -> u.getTeam().getName()));

        List<TeamInsight> result = new ArrayList<>();
        for (Map.Entry<String, List<User>> entry : byTeam.entrySet()) {
            String teamName = entry.getKey();
            List<User> members = entry.getValue();
            long totalMembers = members.size();
            long totalLeaders = members.stream().filter(user -> user.getTeam().isLeader()).count();
            long completedProjects = members.stream().flatMap(user -> user.getTeam().getProjects().stream().filter(Project::isCompleted)).count();
            long activeUsers = members.stream().filter(User::isActive).count();
            double percentActive = totalMembers == 0 ? 0.0 : (activeUsers * 100.0 / totalMembers);

            result.add(new TeamInsight(teamName, totalMembers, totalLeaders, completedProjects, percentActive));
        }
        return result;
    }

    public Map<LocalDate, Long> activeUsersPerDay(Optional<Long> minLogins) {
        List<User> users = repository.findAll();

        Stream<LogEntry> logins = users.stream().flatMap(user -> user.getLogs().stream().filter(logEntry -> logEntry.getAction().equalsIgnoreCase("login")));

        Map<LocalDate, Long> counts = logins.collect(Collectors.groupingBy(LogEntry::getDate, Collectors.counting()));

        if (minLogins.isPresent()) {
            Long min = minLogins.get();
            return counts.entrySet().stream().filter(e -> e.getValue() >= min).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return counts;
    }
}