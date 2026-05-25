package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentReceivedEvent extends Event {

    private final String orderId;
    private final BigDecimal amountGBP;
    private final String paymentMethod;

    public PaymentReceivedEvent(String orderId, BigDecimal amountGBP, String paymentMethod) {
        super("PAYMENT_RECEIVED", LocalDateTime.now());
        this.orderId = orderId;
        this.amountGBP = amountGBP;
        this.paymentMethod = paymentMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmountGBP() {
        return amountGBP;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
