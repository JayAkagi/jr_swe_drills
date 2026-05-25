package com.practice.model;

import java.math.BigDecimal;

public class CommissionResult {
    private final String salespersonId;
    private final BigDecimal totalSalesGBP;
    private final BigDecimal targetGBP;
    private final BigDecimal achievementPercent;
    private final BigDecimal commissionGBP;
    private final String tierReached;

    public CommissionResult(String salespersonId, BigDecimal totalSalesGBP, BigDecimal targetGBP,
                            BigDecimal achievementPercent, BigDecimal commissionGBP, String tierReached) {
        this.salespersonId = salespersonId;
        this.totalSalesGBP = totalSalesGBP;
        this.targetGBP = targetGBP;
        this.achievementPercent = achievementPercent;
        this.commissionGBP = commissionGBP;
        this.tierReached = tierReached;
    }

    public String getSalespersonId() { return salespersonId; }
    public BigDecimal getTotalSalesGBP() { return totalSalesGBP; }
    public BigDecimal getTargetGBP() { return targetGBP; }
    public BigDecimal getAchievementPercent() { return achievementPercent; }
    public BigDecimal getCommissionGBP() { return commissionGBP; }
    public String getTierReached() { return tierReached; }
}
