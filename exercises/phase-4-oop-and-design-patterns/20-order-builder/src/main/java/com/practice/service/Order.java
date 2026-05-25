package com.practice.service;

import com.practice.model.OrderItem;

import java.util.List;

public class Order {

    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final String deliveryAddress;
    private final String billingAddress;
    private final String paymentMethod;
    private final String discountCode;
    private final String priority;
    private final String notes;

    private Order(Builder builder) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getOrderId() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getCustomerId() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<OrderItem> getItems() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getDeliveryAddress() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getBillingAddress() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getPaymentMethod() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getDiscountCode() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getPriority() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getNotes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static class Builder {

        private String orderId;
        private String customerId;
        private List<OrderItem> items;
        private String deliveryAddress;
        private String billingAddress;
        private String paymentMethod;
        private String discountCode;
        private String priority;
        private String notes;

        public Builder orderId(String orderId) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder customerId(String customerId) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder items(List<OrderItem> items) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder deliveryAddress(String deliveryAddress) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder billingAddress(String billingAddress) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder paymentMethod(String paymentMethod) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder discountCode(String discountCode) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder priority(String priority) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Builder notes(String notes) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Order build() {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
