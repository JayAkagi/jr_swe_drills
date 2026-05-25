package com.practice.model;

import java.time.LocalDateTime;

public class ApprovalAction {

    private final ApprovalStage stage;
    private final ApprovalDecision decision;
    private final String actorName;
    private final String comment;
    private final LocalDateTime actionedAt;

    public ApprovalAction(ApprovalStage stage, ApprovalDecision decision, String actorName,
                          String comment, LocalDateTime actionedAt) {
        this.stage = stage;
        this.decision = decision;
        this.actorName = actorName;
        this.comment = comment;
        this.actionedAt = actionedAt;
    }

    public ApprovalStage getStage() {
        return stage;
    }

    public ApprovalDecision getDecision() {
        return decision;
    }

    public String getActorName() {
        return actorName;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getActionedAt() {
        return actionedAt;
    }
}
