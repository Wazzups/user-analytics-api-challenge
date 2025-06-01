package com.wazzups.analytics.userapi.controller;

import com.wazzups.analytics.userapi.Exception.InvalidJsonException;
import com.wazzups.analytics.userapi.dto.TeamInsight;
import com.wazzups.analytics.userapi.model.User;
import com.wazzups.analytics.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Import users")
    @PostMapping
    public ResponseEntity<Void> importUsers(@RequestBody List<User> users) {
        userService.importUsers(users);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Import users from json file")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            userService.importUsersFromJsonFile(file);
            return ResponseEntity.ok().build();
        } catch (InvalidJsonException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Return users with score >= 900 and isActive")
    @GetMapping("/superusers")
    public ResponseEntity<List<User>> getSuperUsers() {
        log.info("222");
        List<User> superUsers = userService.superUsers();
        return ResponseEntity.ok(superUsers);
    }

    @Operation(summary = "Return limit countries with most superusers")
    @GetMapping("/top-countries")
    public ResponseEntity<Map<String, Long>> topCountries(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        Map<String, Long> topCountries = userService.topCountries(limit);
        return ResponseEntity.ok(topCountries);
    }

    @Operation(summary = "Return team insights/statistics")
    @GetMapping("/team-insights")
    public ResponseEntity<List<TeamInsight>> teamInsights() {
        List<TeamInsight> teamInsights = userService.teamInsights();
        return ResponseEntity.ok(teamInsights);
    }

    @Operation(summary = "Return active users per day with min of logins per day")
    @GetMapping("/active-users-per-day")
    public ResponseEntity<Map<LocalDate, Long>> activeUsersPerDay(@RequestParam(name = "min", required = false) Optional<Long> min) {
        Map<LocalDate, Long> usersPerDay = userService.activeUsersPerDay(min);
        return ResponseEntity.ok(usersPerDay);
    }
}
