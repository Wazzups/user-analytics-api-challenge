package com.wazzups.analytics.userapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wazzups.analytics.userapi.dto.EvaluationResult;
import com.wazzups.analytics.userapi.model.User;
import com.wazzups.analytics.userapi.util.JsonLoader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EvaluationService {

    private final RestTemplate restTemplate;

    @Autowired
    public EvaluationService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    public List<EvaluationResult> runAllTests() {
        List<EvaluationResult> results = new ArrayList<>();

        EvaluationResult er1 = testGet("/users/superusers");
        results.add(er1);

        EvaluationResult er2 = testGet("/users/top-countries");
        results.add(er2);

        EvaluationResult er3 = testGet("/users/team-insights");
        results.add(er3);

        EvaluationResult er4 = testGet("/users/active-users-per-day");
        results.add(er4);

        EvaluationResult er5 = testPost();
        results.add(er5);

        return results;
    }

    private EvaluationResult testGet(String path) {
        String url = "http://localhost8081" + path;
        Instant start = Instant.now();

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception ex) {
            return new EvaluationResult(url, false, 0, false);
        }

        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();
        HttpStatusCode statusCode = response.getStatusCode();
        boolean isValidJson;
        try {
            new ObjectMapper().readTree(response.getBody());
            isValidJson = true;
        } catch (Exception e) {
            isValidJson = false;
        }

        return new EvaluationResult(url, statusCode.is2xxSuccessful(), millis, isValidJson);
    }

    private EvaluationResult testPost() {
        String url = "http://localhost8081" + "/users";

        try {
            List<User> users = JsonLoader.loadUsersFromJsonFile("usuarios_1000.json");
            HttpEntity<List<User>> entity = new HttpEntity<>(users);
            Instant start = Instant.now();

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();
            HttpStatusCode statusCode = response.getStatusCode();

            boolean validJson;
            try {
                new ObjectMapper().readTree(response.getBody() == null ? "{}" : response.getBody());
                validJson = true;
            } catch (IOException ex) {
                validJson = false;
            }

            return new EvaluationResult(url, statusCode.is2xxSuccessful(), duration, validJson);
        } catch (IOException e) {
            return new EvaluationResult(url, false, 0, false);
        }

    }

}
