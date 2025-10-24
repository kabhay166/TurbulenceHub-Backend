package com.example.spring_example.entity;

import lombok.Getter;
import lombok.Setter;

import java.lang.Process;

@Getter
@Setter
public class RunProcessInfo {
    java.lang.Process process;
    String username;
    String kind;
    String dimension;
    String resolution;
    Long runId;
    String timeOfRun;
    String processInfoId;
    boolean completed = false;

    public RunProcessInfo(Process process, String username, String kind, String dimension, String resolution, Long runId, String timeOfRun, String processInfoId) {
        this.process = process;
        this.username = username;
        this.kind = kind;
        this.dimension = dimension;
        this.resolution = resolution;
        this.runId = runId;
        this.timeOfRun = timeOfRun;
        this.processInfoId = processInfoId;
    }

}