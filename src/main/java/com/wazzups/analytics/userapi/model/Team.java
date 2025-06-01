package com.wazzups.analytics.userapi.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Team {

    private String name;
    private boolean leader;
    private List<Project> projects;
}
