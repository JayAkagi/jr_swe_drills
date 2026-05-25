package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceValidatorTest {

    private InvoiceValidator validator;
    private static final LocalDate INVOICE_DATE = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        validator = new InvoiceValidator();
    }

    // --- Rule 1: Quantity must be > 0 ---

    @Test
    void shouldReject_whenLineItemHasZeroQuantity() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 0, "10.00", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "quantity"));
    }

    @Test
    void shouldReject_whenLineItemHasNegativeQuantity() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", -1, "10.00", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "quantity"));
    }

    @Test
    void shouldApprove_whenAllQuantitiesArePositive() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS),
                item("Service", 5, "50.00", 20, LineItemCategory.SERVICES)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertTrue(result.isApproved());
    }

    // --- Rule 2: VAT rate must be 0, 5, or 20 ---

    @Test
    void shouldReject_whenVatRateIsInvalid() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 10, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "vat rate"));
    }

    @Test
    void shouldApprove_whenVatRateIsZero() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 0, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    @Test
    void shouldApprove_whenVatRateIsFive() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 5, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    @Test
    void shouldApprove_whenVatRateIsTwenty() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    // --- Rule 3: Unit price cannot exceed category cap ---

    @Test
    void shouldReject_whenGoodsPriceExceedsLimit() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Expensive Widget", 1, "1000.01", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    @Test
    void shouldApprove_whenGoodsPriceIsExactlyAtLimit() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "1000.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    @Test
    void shouldReject_whenServicesPriceExceedsLimit() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Consulting Day", 1, "500.01", 20, LineItemCategory.SERVICES)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    @Test
    void shouldReject_whenSubscriptionPriceExceedsLimit() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Monthly Plan", 1, "200.01", 20, LineItemCategory.SUBSCRIPTION)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    @Test
    void shouldReject_whenConsultingPriceExceedsLimit() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Strategy Session", 1, "800.01", 20, LineItemCategory.CONSULTING)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    // --- Rule 4: Payment due must not be more than 60 days after invoice date ---

    @Test
    void shouldReject_whenPaymentDueMoreThan60DaysAfterInvoice() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(61), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "payment terms"));
    }

    @Test
    void shouldApprove_whenPaymentDueExactly60DaysAfterInvoice() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(60), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    @Test
    void shouldApprove_whenPaymentDueWithin60Days() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    // --- Rule 5: Duplicate line items ---

    @Test
    void shouldReject_whenTwoItemsHaveSameDescriptionAndPrice() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS),
                item("Widget", 2, "10.00", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "duplicate"));
    }

    @Test
    void shouldApprove_whenItemsHaveSameDescriptionButDifferentPrice() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS),
                item("Widget", 1, "20.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    @Test
    void shouldApprove_whenItemsHaveDifferentDescriptionButSamePrice() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget A", 1, "10.00", 20, LineItemCategory.GOODS),
                item("Widget B", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        assertTrue(validator.validate(invoice).isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBroken() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(90), List.of(
                item("Widget", 0, "10.00", 15, LineItemCategory.GOODS),
                item("Widget", 1, "10.00", 20, LineItemCategory.GOODS)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "quantity"));
        assertTrue(hasViolationContaining(result, "vat rate"));
        assertTrue(hasViolationContaining(result, "payment terms"));
        assertTrue(hasViolationContaining(result, "duplicate"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        Invoice invoice = invoice(INVOICE_DATE, INVOICE_DATE.plusDays(30), List.of(
                item("Widget", 2, "50.00", 20, LineItemCategory.GOODS),
                item("Support", 1, "100.00", 20, LineItemCategory.SERVICES)
        ));
        InvoiceValidationResult result = validator.validate(invoice);
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private Invoice invoice(LocalDate invoiceDate, LocalDate dueDate, List<LineItem> items) {
        return new Invoice("Acme Ltd", invoiceDate, dueDate, items);
    }

    private LineItem item(String description, int quantity, String price, int vatRate, LineItemCategory category) {
        return new LineItem(description, quantity, new BigDecimal(price), vatRate, category);
    }

    private boolean hasViolationContaining(InvoiceValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
