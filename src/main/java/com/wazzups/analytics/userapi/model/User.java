package com.wazzups.analytics.userapi.model;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {

    private UUID id;
    private String name;
    private int age;
    private int score;
    private boolean active;
    private String country;
    private Team team;
    private List<LogEntry> logs;
}
