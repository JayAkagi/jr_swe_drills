package com.practice.service;

import com.practice.model.BenefitDeduction;
import com.practice.model.Employee;
import com.practice.model.Payslip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PayslipGeneratorTest {

    private PayslipGenerator generator;
    private final YearMonth PERIOD = YearMonth.of(2024, 6);

    @BeforeEach
    void setUp() {
        generator = new PayslipGenerator();
    }

    private Employee employee(String id, String name, String annualSalary, String pensionPct,
                              List<BenefitDeduction> benefits) {
        return new Employee(id, name, new BigDecimal(annualSalary), "1257L",
                new BigDecimal(pensionPct), benefits);
    }

    @Test
    void shouldSetEmployeeId_whenGenerating() {
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals("EMP001", slip.getEmployeeId());
    }

    @Test
    void shouldSetName_whenGenerating() {
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals("Alice", slip.getName());
    }

    @Test
    void shouldSetPeriod_whenGenerating() {
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals(PERIOD, slip.getPeriod());
    }

    @Test
    void shouldComputeGrossPay_asAnnualSalaryDividedByTwelve() {
        Employee emp = employee("EMP001", "Alice", "36000", "0", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals(0, new BigDecimal("3000.00").compareTo(slip.getGrossPayGBP()));
    }

    @Test
    void shouldHaveZeroTaxAndNI_whenSalaryBelowPersonalAllowance() {
        Employee emp = employee("EMP001", "Alice", "9000", "0", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals(0, BigDecimal.ZERO.compareTo(slip.getIncomeTaxGBP()));
        assertEquals(0, BigDecimal.ZERO.compareTo(slip.getNationalInsuranceGBP()));
    }

    @Test
    void shouldComputeBasicRateTax_whenSalaryInBasicBand() {
        Employee emp = employee("EMP001", "Alice", "36000", "0", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal grossPay = new BigDecimal("3000.00");
        BigDecimal taxableInBasicBand = grossPay.subtract(new BigDecimal("1048.00"));
        BigDecimal expectedTax = taxableInBasicBand.multiply(new BigDecimal("0.20"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedTax.compareTo(slip.getIncomeTaxGBP()));
    }

    @Test
    void shouldComputeNI_whenSalaryInBasicBand() {
        Employee emp = employee("EMP001", "Alice", "36000", "0", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal grossPay = new BigDecimal("3000.00");
        BigDecimal niableInBasicBand = grossPay.subtract(new BigDecimal("1048.00"));
        BigDecimal expectedNI = niableInBasicBand.multiply(new BigDecimal("0.12"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedNI.compareTo(slip.getNationalInsuranceGBP()));
    }

    @Test
    void shouldComputeTax_whenSalaryAboveHigherRateBand() {
        Employee emp = employee("EMP001", "Alice", "72000", "0", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal grossPay = new BigDecimal("6000.00");
        BigDecimal basicBandTax = new BigDecimal("4189.00").subtract(new BigDecimal("1048.00"))
                .multiply(new BigDecimal("0.20"));
        BigDecimal higherBandTax = grossPay.subtract(new BigDecimal("4189.00"))
                .multiply(new BigDecimal("0.40"));
        BigDecimal expectedTax = basicBandTax.add(higherBandTax)
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedTax.compareTo(slip.getIncomeTaxGBP()));
    }

    @Test
    void shouldComputePensionDeduction_asPercentOfGrossPay() {
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal grossPay = new BigDecimal("3000.00");
        BigDecimal expectedPension = grossPay.multiply(new BigDecimal("5"))
                .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedPension.compareTo(slip.getPensionDeductionGBP()));
    }

    @Test
    void shouldDeductBenefits_fromNetPay() {
        BenefitDeduction health = new BenefitDeduction("Health", new BigDecimal("50.00"));
        BenefitDeduction gym = new BenefitDeduction("Gym", new BigDecimal("30.00"));
        Employee emp = employee("EMP001", "Alice", "9000", "0", List.of(health, gym));
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal grossPay = new BigDecimal("750.00");
        BigDecimal expectedNet = grossPay.subtract(new BigDecimal("80.00"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedNet.compareTo(slip.getNetPayGBP()));
    }

    @Test
    void shouldComputeNetPay_asGrossMinusAllDeductions() {
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of());
        Payslip slip = generator.generate(emp, PERIOD);
        BigDecimal expectedNet = slip.getGrossPayGBP()
                .subtract(slip.getIncomeTaxGBP())
                .subtract(slip.getNationalInsuranceGBP())
                .subtract(slip.getPensionDeductionGBP());
        assertEquals(0, expectedNet.compareTo(slip.getNetPayGBP()));
    }

    @Test
    void shouldCopyOtherDeductions_toPayslip() {
        BenefitDeduction benefit = new BenefitDeduction("Dental", new BigDecimal("20.00"));
        Employee emp = employee("EMP001", "Alice", "36000", "5", List.of(benefit));
        Payslip slip = generator.generate(emp, PERIOD);
        assertEquals(1, slip.getOtherDeductions().size());
        assertEquals("Dental", slip.getOtherDeductions().get(0).getName());
    }
}
