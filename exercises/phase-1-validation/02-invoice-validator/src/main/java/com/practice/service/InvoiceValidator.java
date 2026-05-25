package com.practice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.practice.model.Invoice;
import com.practice.model.InvoiceValidationResult;
import com.practice.model.Violation;
import com.practice.model.LineItem;
import com.practice.model.LineItemCategory;

public class InvoiceValidator {

    public InvoiceValidationResult validate(Invoice invoice) {
        List<LineItem> invoiceLines = invoice.getLineItems();
        List<Violation> violations = new ArrayList<>();

        validateQuantity(invoiceLines, violations);
        validateVATRate(invoiceLines, violations);
        validateCategoryPriceLimit(invoiceLines, violations);
        validatePaymentTerms(invoice, violations);
        validateDuplicateInvoice(invoiceLines, violations);

        return (violations.isEmpty()) ? InvoiceValidationResult.approved() : InvoiceValidationResult.rejected(violations); 
    }

    public void validateQuantity(List<LineItem> invoiceLines, List<Violation> violations){
        for (LineItem invoiceLine : invoiceLines) {
            if (invoiceLine.getQuantity() <= 0) {
                violations.add(
                    new Violation("Invalid invoice quantity.")
                );
            }
        }
    }

    public void validateVATRate(List<LineItem> invoiceLines, List<Violation> violations){
        int[] vatRates = {0, 5, 20};
        for (LineItem invoiceLine : invoiceLines) {
            boolean validVAT = false;
            for (int vatRate : vatRates) {
                if (invoiceLine.getVatRatePercent() == vatRate) {
                    validVAT = true;
                    break;
                }
            }

            if (validVAT == false) {
                violations.add(
                    new Violation("Invoice invalid vat rate.")
                );
            }
        }
    }

    public void validateCategoryPriceLimit(List<LineItem> invoiceLines, List<Violation> violations){
        Map<LineItemCategory, BigDecimal> categoryPriceLimit = new HashMap<>(Map.of(
            LineItemCategory.GOODS, new BigDecimal("1000"),
            LineItemCategory.SERVICES, new BigDecimal("500"),
            LineItemCategory.SUBSCRIPTION, new BigDecimal("200"),
            LineItemCategory.CONSULTING, new BigDecimal("800")
        ));
        for (LineItem invoiceLine : invoiceLines) {
            LineItemCategory category = invoiceLine.getCategory();
            BigDecimal limit = categoryPriceLimit.get(category);

            if (invoiceLine.getUnitPriceGBP().compareTo(limit) > 0) {
                violations.add(
                    new Violation("limit exceeded.")
                );
            }
        }
    }

    public void validatePaymentTerms(Invoice invoice, List<Violation> violations){
        if (invoice.getPaymentDueDate().isAfter(invoice.getInvoiceDate().plusDays(60))) {
            violations.add(
                new Violation("Invalid payment terms. Cannot be more than 60 days from invoice date.")
            );
        }
    }

    public void validateDuplicateInvoice(List<LineItem> invoiceLines, List<Violation> violations){
        for (int i = 0; i < invoiceLines.size() -1; i++) {
            for (int j = i + 1; j < invoiceLines.size(); j++) {
                if (
                    invoiceLines.get(i).getDescription().equals(invoiceLines.get(j).getDescription()) &&
                    invoiceLines.get(i).getUnitPriceGBP().compareTo(invoiceLines.get(j).getUnitPriceGBP()) == 0
                ) {
                    violations.add(
                        new Violation("Invoice duplicate")
                    );
                }
            }
        }
    }
}
