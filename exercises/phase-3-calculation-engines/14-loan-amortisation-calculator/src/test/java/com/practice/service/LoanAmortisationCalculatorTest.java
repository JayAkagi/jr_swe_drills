package com.practice.service;

import com.practice.model.AmortisationSchedule;
import com.practice.model.LoanDetails;
import com.practice.model.RepaymentRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanAmortisationCalculatorTest {

    private LoanAmortisationCalculator calculator;
    private static final LocalDate START = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        calculator = new LoanAmortisationCalculator();
    }

    @Test
    void shouldProduceTwelveRows_whenTermIsOneYear() {
        LoanDetails loan = new LoanDetails(new BigDecimal("12000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        assertEquals(12, schedule.getRows().size());
    }

    @Test
    void shouldProduceTwentyFourRows_whenTermIsTwoYears() {
        LoanDetails loan = new LoanDetails(new BigDecimal("12000.00"), new BigDecimal("6.00"), 24, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        assertEquals(24, schedule.getRows().size());
    }

    @Test
    void shouldHaveOpeningBalanceEqualToPrincipal_whenFirstRow() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("5.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        assertEquals(0, new BigDecimal("10000.00").compareTo(schedule.getRows().get(0).getOpeningBalanceGBP()));
    }

    @Test
    void shouldHaveZeroClosingBalance_whenFinalRow() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("5.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        RepaymentRow lastRow = schedule.getRows().get(schedule.getRows().size() - 1);
        assertEquals(0, BigDecimal.ZERO.compareTo(lastRow.getClosingBalanceGBP()));
    }

    @Test
    void shouldIncrementPaymentDatesByOneMonth_whenRowsAreGenerated() {
        LoanDetails loan = new LoanDetails(new BigDecimal("5000.00"), new BigDecimal("4.00"), 6, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        for (int i = 0; i < schedule.getRows().size(); i++) {
            assertEquals(START.plusMonths(i + 1), schedule.getRows().get(i).getPaymentDate());
        }
    }

    @Test
    void shouldHaveTotalInterestEqualToSumOfRowInterests() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        BigDecimal sumInterest = schedule.getRows().stream()
                .map(RepaymentRow::getInterestGBP)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, sumInterest.compareTo(schedule.getTotalInterestGBP()));
    }

    @Test
    void shouldHaveConsistentMonthlyPayment_whenNotLastRow() {
        LoanDetails loan = new LoanDetails(new BigDecimal("12000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        BigDecimal expected = schedule.getMonthlyPaymentGBP();
        for (int i = 0; i < schedule.getRows().size() - 1; i++) {
            RepaymentRow row = schedule.getRows().get(i);
            BigDecimal rowPayment = row.getInterestGBP().add(row.getPrincipalRepaidGBP());
            assertEquals(0, expected.compareTo(rowPayment));
        }
    }

    @Test
    void shouldHaveInterestDecreasingOverTime() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        for (int i = 1; i < schedule.getRows().size() - 1; i++) {
            assertTrue(schedule.getRows().get(i).getInterestGBP()
                    .compareTo(schedule.getRows().get(i - 1).getInterestGBP()) <= 0);
        }
    }

    @Test
    void shouldHaveMonthNumbersOneToN() {
        LoanDetails loan = new LoanDetails(new BigDecimal("5000.00"), new BigDecimal("5.00"), 6, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        for (int i = 0; i < schedule.getRows().size(); i++) {
            assertEquals(i + 1, schedule.getRows().get(i).getMonth());
        }
    }

    @Test
    void shouldHaveTotalPaidEqualToSumOfAllPayments() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        BigDecimal sumPaid = schedule.getRows().stream()
                .map(r -> r.getInterestGBP().add(r.getPrincipalRepaidGBP()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, sumPaid.compareTo(schedule.getTotalPaidGBP()));
    }

    @Test
    void shouldHaveClosingBalanceDecreasing_whenPrincipalRepaidEachMonth() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        for (int i = 1; i < schedule.getRows().size(); i++) {
            assertTrue(schedule.getRows().get(i).getClosingBalanceGBP()
                    .compareTo(schedule.getRows().get(i - 1).getClosingBalanceGBP()) < 0);
        }
    }

    @Test
    void shouldHaveMonthlyPaymentGreaterThanInterestOnFirstRow() {
        LoanDetails loan = new LoanDetails(new BigDecimal("10000.00"), new BigDecimal("6.00"), 12, START);
        AmortisationSchedule schedule = calculator.calculate(loan);
        RepaymentRow first = schedule.getRows().get(0);
        assertTrue(schedule.getMonthlyPaymentGBP().compareTo(first.getInterestGBP()) > 0);
    }
}
