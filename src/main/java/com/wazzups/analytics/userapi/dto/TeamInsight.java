package com.wazzups.analytics.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamInsight {
    private String teamName;
    private long totalMembers;
    private long totalLeaders;
    private long projectsCompleted;
    private double percentActive;
}
