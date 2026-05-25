package com.practice.model;

import java.util.List;

public class ExecutionPlan {
    private final List<String> orderedTaskIds;
    private final List<String> warnings;

    public ExecutionPlan(List<String> orderedTaskIds, List<String> warnings) {
        this.orderedTaskIds = orderedTaskIds;
        this.warnings = warnings;
    }

    public List<String> getOrderedTaskIds() { return orderedTaskIds; }
    public List<String> getWarnings() { return warnings; }
}
