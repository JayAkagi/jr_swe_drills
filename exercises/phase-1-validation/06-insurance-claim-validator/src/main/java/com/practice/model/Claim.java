package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Claim {

    private final String policyNumber;
    private final String claimantName;
    private final LocalDate incidentDate;
    private final LocalDate submissionDate;
    private final ClaimType claimType;
    private final List<ClaimItem> items;
    private final BigDecimal excessGBP;

    public Claim(String policyNumber, String claimantName, LocalDate incidentDate, LocalDate submissionDate,
                 ClaimType claimType, List<ClaimItem> items, BigDecimal excessGBP) {
        this.policyNumber = policyNumber;
        this.claimantName = claimantName;
        this.incidentDate = incidentDate;
        this.submissionDate = submissionDate;
        this.claimType = claimType;
        this.items = items;
        this.excessGBP = excessGBP;
    }

    public String getPolicyNumber() { return policyNumber; }
    public String getClaimantName() { return claimantName; }
    public LocalDate getIncidentDate() { return incidentDate; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public ClaimType getClaimType() { return claimType; }
    public List<ClaimItem> getItems() { return items; }
    public BigDecimal getExcessGBP() { return excessGBP; }
}
