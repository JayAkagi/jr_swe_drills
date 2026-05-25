package com.practice.service;

import com.practice.model.CommissionResult;
import com.practice.model.SaleRecord;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class CommissionCalculator {

    public List<CommissionResult> calculate(List<SaleRecord> sales, Map<String, BigDecimal> targets, YearMonth period) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
