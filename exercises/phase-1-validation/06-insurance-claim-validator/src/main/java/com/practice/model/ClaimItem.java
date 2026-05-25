package com.practice.model;

import java.math.BigDecimal;

public class ClaimItem {

    private final String description;
    private final BigDecimal claimAmountGBP;
    private final int itemAgeYears;
    private final RepairType replacementOrRepair;
    private final boolean evidenceAttached;

    public ClaimItem(String description, BigDecimal claimAmountGBP, int itemAgeYears,
                     RepairType replacementOrRepair, boolean evidenceAttached) {
        this.description = description;
        this.claimAmountGBP = claimAmountGBP;
        this.itemAgeYears = itemAgeYears;
        this.replacementOrRepair = replacementOrRepair;
        this.evidenceAttached = evidenceAttached;
    }

    public String getDescription() { return description; }
    public BigDecimal getClaimAmountGBP() { return claimAmountGBP; }
    public int getItemAgeYears() { return itemAgeYears; }
    public RepairType getReplacementOrRepair() { return replacementOrRepair; }
    public boolean isEvidenceAttached() { return evidenceAttached; }
}
