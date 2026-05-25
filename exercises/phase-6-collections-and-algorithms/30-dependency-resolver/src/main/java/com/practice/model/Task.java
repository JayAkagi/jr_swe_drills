package com.practice.model;

import java.util.List;

public class Task {
    private final String taskId;
    private final String name;
    private final List<String> dependsOn;

    public Task(String taskId, String name, List<String> dependsOn) {
        this.taskId = taskId;
        this.name = name;
        this.dependsOn = dependsOn;
    }

    public String getTaskId() { return taskId; }
    public String getName() { return name; }
    public List<String> getDependsOn() { return dependsOn; }
}
