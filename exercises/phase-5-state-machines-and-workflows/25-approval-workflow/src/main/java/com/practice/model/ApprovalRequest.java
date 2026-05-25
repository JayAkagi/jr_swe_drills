package com.practice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApprovalRequest {

    private final String requestId;
    private final String submittedBy;
    private final BigDecimal totalAmountGBP;
    private ApprovalStage currentStage;
    private List<ApprovalAction> history;

    public ApprovalRequest(String requestId, String submittedBy, BigDecimal totalAmountGBP) {
        this.requestId = requestId;
        this.submittedBy = submittedBy;
        this.totalAmountGBP = totalAmountGBP;
        this.currentStage = ApprovalStage.MANAGER_REVIEW;
        this.history = new ArrayList<>();
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public BigDecimal getTotalAmountGBP() {
        return totalAmountGBP;
    }

    public ApprovalStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(ApprovalStage currentStage) {
        this.currentStage = currentStage;
    }

    public List<ApprovalAction> getHistory() {
        return history;
    }
}
