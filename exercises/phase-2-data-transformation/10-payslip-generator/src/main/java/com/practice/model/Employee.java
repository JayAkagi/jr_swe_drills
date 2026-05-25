package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class Employee {
    private final String employeeId;
    private final String name;
    private final BigDecimal annualSalaryGBP;
    private final String taxCode;
    private final BigDecimal pensionContributionPercent;
    private final List<BenefitDeduction> benefitDeductions;

    public Employee(String employeeId, String name, BigDecimal annualSalaryGBP, String taxCode,
                    BigDecimal pensionContributionPercent, List<BenefitDeduction> benefitDeductions) {
        this.employeeId = employeeId;
        this.name = name;
        this.annualSalaryGBP = annualSalaryGBP;
        this.taxCode = taxCode;
        this.pensionContributionPercent = pensionContributionPercent;
        this.benefitDeductions = benefitDeductions;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public BigDecimal getAnnualSalaryGBP() { return annualSalaryGBP; }
    public String getTaxCode() { return taxCode; }
    public BigDecimal getPensionContributionPercent() { return pensionContributionPercent; }
    public List<BenefitDeduction> getBenefitDeductions() { return benefitDeductions; }
}
