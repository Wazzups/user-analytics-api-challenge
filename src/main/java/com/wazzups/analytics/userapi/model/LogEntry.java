package com.wazzups.analytics.userapi.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogEntry {
    private LocalDate date;
    private String action;
}
