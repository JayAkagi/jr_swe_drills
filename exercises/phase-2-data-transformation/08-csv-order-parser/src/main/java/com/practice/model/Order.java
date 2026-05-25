package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private final String orderId;
    private final String customerName;
    private final String productCode;
    private final int quantity;
    private final BigDecimal priceGBP;
    private final LocalDate orderDate;

    public Order(String orderId, String customerName, String productCode,
                 int quantity, BigDecimal priceGBP, LocalDate orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.priceGBP = priceGBP;
        this.orderDate = orderDate;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getProductCode() { return productCode; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceGBP() { return priceGBP; }
    public LocalDate getOrderDate() { return orderDate; }
}
