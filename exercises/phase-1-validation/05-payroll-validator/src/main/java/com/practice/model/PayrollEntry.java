package com.practice.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class PayrollEntry {

    private final String employeeId;
    private final String employeeName;
    private final Department department;
    private final BigDecimal basicSalaryGBP;
    private final BigDecimal bonusGBP;
    private final BigDecimal deductionsGBP;
    private final YearMonth payPeriod;

    public PayrollEntry(String employeeId, String employeeName, Department department,
                        BigDecimal basicSalaryGBP, BigDecimal bonusGBP, BigDecimal deductionsGBP,
                        YearMonth payPeriod) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.basicSalaryGBP = basicSalaryGBP;
        this.bonusGBP = bonusGBP;
        this.deductionsGBP = deductionsGBP;
        this.payPeriod = payPeriod;
    }

    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public Department getDepartment() { return department; }
    public BigDecimal getBasicSalaryGBP() { return basicSalaryGBP; }
    public BigDecimal getBonusGBP() { return bonusGBP; }
    public BigDecimal getDeductionsGBP() { return deductionsGBP; }
    public YearMonth getPayPeriod() { return payPeriod; }
}
