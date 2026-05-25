package com.practice.service;

import com.practice.model.TaxBreakdown;
import com.practice.model.TaxInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorTest {

    private TaxCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TaxCalculator();
    }

    @Test
    void shouldHaveNoIncomeTaxAndNoNI_whenIncomeIsBelowPersonalAllowance() {
        TaxInput input = new TaxInput(new BigDecimal("10000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getTotalIncomeTaxGBP()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getTotalNIGBP()));
    }

    @Test
    void shouldCalculateBasicRateTax_whenIncomeIsInBasicBand() {
        TaxInput input = new TaxInput(new BigDecimal("30000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal taxableIncome = new BigDecimal("30000.00").subtract(new BigDecimal("12570.00"));
        BigDecimal expectedTax = taxableIncome.multiply(new BigDecimal("0.20")).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedTax.compareTo(result.getTotalIncomeTaxGBP()));
    }

    @Test
    void shouldCalculateHigherRateTax_whenIncomeIsInHigherBand() {
        TaxInput input = new TaxInput(new BigDecimal("60000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal basicBandTax = new BigDecimal("37700.00").multiply(new BigDecimal("0.20"));
        BigDecimal higherBandIncome = new BigDecimal("60000.00").subtract(new BigDecimal("12570.00")).subtract(new BigDecimal("37700.00"));
        BigDecimal higherBandTax = higherBandIncome.multiply(new BigDecimal("0.40"));
        BigDecimal expectedTax = basicBandTax.add(higherBandTax).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedTax.compareTo(result.getTotalIncomeTaxGBP()));
    }

    @Test
    void shouldCalculateAdditionalRateTax_whenIncomeExceedsAdditionalThreshold() {
        TaxInput input = new TaxInput(new BigDecimal("200000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        assertTrue(result.getTotalIncomeTaxGBP().compareTo(new BigDecimal("70000")) > 0);
    }

    @Test
    void shouldTaperPersonalAllowance_whenIncomeExceeds100000() {
        TaxInput input = new TaxInput(new BigDecimal("110000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal expectedAllowance = new BigDecimal("12570.00").subtract(new BigDecimal("5000.00"));
        assertEquals(0, expectedAllowance.compareTo(result.getPersonalAllowance()));
    }

    @Test
    void shouldHaveZeroPersonalAllowance_whenIncomeIsVeryHigh() {
        TaxInput input = new TaxInput(new BigDecimal("150000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getPersonalAllowance()));
    }

    @Test
    void shouldAddMarriageAllowance_whenMarriageAllowanceTransferredIsTrue() {
        TaxInput input = new TaxInput(new BigDecimal("20000.00"), 2024, BigDecimal.ZERO, true);
        TaxBreakdown result = calculator.calculate(input);
        assertEquals(0, new BigDecimal("13830.00").compareTo(result.getPersonalAllowance()));
    }

    @Test
    void shouldReduceTaxableIncome_whenPensionContributionIsProvided() {
        TaxInput withPension = new TaxInput(new BigDecimal("30000.00"), 2024, new BigDecimal("5000.00"), false);
        TaxInput withoutPension = new TaxInput(new BigDecimal("30000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown resultWithPension = calculator.calculate(withPension);
        TaxBreakdown resultWithoutPension = calculator.calculate(withoutPension);
        assertTrue(resultWithPension.getTotalIncomeTaxGBP().compareTo(resultWithoutPension.getTotalIncomeTaxGBP()) < 0);
    }

    @Test
    void shouldHaveZeroNI_whenIncomeIsBelowNIThreshold() {
        TaxInput input = new TaxInput(new BigDecimal("10000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getTotalNIGBP()));
    }

    @Test
    void shouldApply8PercentNI_whenIncomeIsInPrimaryNIBand() {
        TaxInput input = new TaxInput(new BigDecimal("30000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal niableIncome = new BigDecimal("30000.00").subtract(new BigDecimal("12570.00"));
        BigDecimal expectedNI = niableIncome.multiply(new BigDecimal("0.08")).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedNI.compareTo(result.getTotalNIGBP()));
    }

    @Test
    void shouldApply2PercentNIAboveUpperEarningsLimit_whenIncomeExceedsUEL() {
        TaxInput input = new TaxInput(new BigDecimal("60000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal primaryNI = new BigDecimal("50270.00").subtract(new BigDecimal("12570.00")).multiply(new BigDecimal("0.08"));
        BigDecimal upperNI = new BigDecimal("60000.00").subtract(new BigDecimal("50270.00")).multiply(new BigDecimal("0.02"));
        BigDecimal expectedNI = primaryNI.add(upperNI).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedNI.compareTo(result.getTotalNIGBP()));
    }

    @Test
    void shouldHaveTakeHomeCorrect_whenAllDeductionsApplied() {
        TaxInput input = new TaxInput(new BigDecimal("30000.00"), 2024, new BigDecimal("2000.00"), false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal expectedTakeHome = new BigDecimal("30000.00")
                .subtract(result.getTotalIncomeTaxGBP())
                .subtract(result.getTotalNIGBP())
                .subtract(new BigDecimal("2000.00"));
        assertEquals(0, expectedTakeHome.compareTo(result.getTakeHomeGBP()));
    }

    @Test
    void shouldPopulateBandsList_whenTaxableIncomeIsAboveZero() {
        TaxInput input = new TaxInput(new BigDecimal("30000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        assertFalse(result.getBands().isEmpty());
    }

    @Test
    void shouldHaveTotalDeductionsEqualToTaxPlusNI_whenCalculated() {
        TaxInput input = new TaxInput(new BigDecimal("40000.00"), 2024, BigDecimal.ZERO, false);
        TaxBreakdown result = calculator.calculate(input);
        BigDecimal expected = result.getTotalIncomeTaxGBP().add(result.getTotalNIGBP());
        assertEquals(0, expected.compareTo(result.getTotalDeductionsGBP()));
    }
}
