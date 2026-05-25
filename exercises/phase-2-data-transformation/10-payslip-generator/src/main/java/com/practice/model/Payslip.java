package com.practice.model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class Payslip {
    private final String employeeId;
    private final String name;
    private final YearMonth period;
    private final BigDecimal grossPayGBP;
    private final BigDecimal incomeTaxGBP;
    private final BigDecimal nationalInsuranceGBP;
    private final BigDecimal pensionDeductionGBP;
    private final List<BenefitDeduction> otherDeductions;
    private final BigDecimal netPayGBP;

    public Payslip(String employeeId, String name, YearMonth period, BigDecimal grossPayGBP,
                   BigDecimal incomeTaxGBP, BigDecimal nationalInsuranceGBP, BigDecimal pensionDeductionGBP,
                   List<BenefitDeduction> otherDeductions, BigDecimal netPayGBP) {
        this.employeeId = employeeId;
        this.name = name;
        this.period = period;
        this.grossPayGBP = grossPayGBP;
        this.incomeTaxGBP = incomeTaxGBP;
        this.nationalInsuranceGBP = nationalInsuranceGBP;
        this.pensionDeductionGBP = pensionDeductionGBP;
        this.otherDeductions = otherDeductions;
        this.netPayGBP = netPayGBP;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public YearMonth getPeriod() { return period; }
    public BigDecimal getGrossPayGBP() { return grossPayGBP; }
    public BigDecimal getIncomeTaxGBP() { return incomeTaxGBP; }
    public BigDecimal getNationalInsuranceGBP() { return nationalInsuranceGBP; }
    public BigDecimal getPensionDeductionGBP() { return pensionDeductionGBP; }
    public List<BenefitDeduction> getOtherDeductions() { return otherDeductions; }
    public BigDecimal getNetPayGBP() { return netPayGBP; }
}
