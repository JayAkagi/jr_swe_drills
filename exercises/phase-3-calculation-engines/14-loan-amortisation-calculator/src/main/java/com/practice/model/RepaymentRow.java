package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RepaymentRow {
    private final int month;
    private final LocalDate paymentDate;
    private final BigDecimal openingBalanceGBP;
    private final BigDecimal interestGBP;
    private final BigDecimal principalRepaidGBP;
    private final BigDecimal closingBalanceGBP;

    public RepaymentRow(int month, LocalDate paymentDate, BigDecimal openingBalanceGBP,
                        BigDecimal interestGBP, BigDecimal principalRepaidGBP, BigDecimal closingBalanceGBP) {
        this.month = month;
        this.paymentDate = paymentDate;
        this.openingBalanceGBP = openingBalanceGBP;
        this.interestGBP = interestGBP;
        this.principalRepaidGBP = principalRepaidGBP;
        this.closingBalanceGBP = closingBalanceGBP;
    }

    public int getMonth() { return month; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public BigDecimal getOpeningBalanceGBP() { return openingBalanceGBP; }
    public BigDecimal getInterestGBP() { return interestGBP; }
    public BigDecimal getPrincipalRepaidGBP() { return principalRepaidGBP; }
    public BigDecimal getClosingBalanceGBP() { return closingBalanceGBP; }
}
