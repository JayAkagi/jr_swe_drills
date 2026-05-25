package com.practice.model;

import java.time.LocalDate;
import java.util.List;

public class Invoice {

    private final String supplierName;
    private final LocalDate invoiceDate;
    private final LocalDate paymentDueDate;
    private final List<LineItem> lineItems;

    public Invoice(String supplierName, LocalDate invoiceDate, LocalDate paymentDueDate, List<LineItem> lineItems) {
        this.supplierName = supplierName;
        this.invoiceDate = invoiceDate;
        this.paymentDueDate = paymentDueDate;
        this.lineItems = lineItems;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
}
