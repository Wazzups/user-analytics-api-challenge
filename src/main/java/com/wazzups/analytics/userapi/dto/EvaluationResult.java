package com.wazzups.analytics.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvaluationResult {
    private String endpoint;
    private boolean statusOk;
    private long responseTimeMs;
    private boolean jsonValid;
}
