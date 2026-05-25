package com.practice.service;

import com.practice.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderBuilderTest {

    private List<OrderItem> validItems;
    private Order.Builder validBuilder;

    @BeforeEach
    void setUp() {
        validItems = List.of(
                new OrderItem("prod-1", 2, new BigDecimal("9.99")),
                new OrderItem("prod-2", 1, new BigDecimal("19.99"))
        );

        validBuilder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .items(validItems)
                .deliveryAddress("123 Main St, London")
                .billingAddress("123 Main St, London")
                .paymentMethod("CREDIT_CARD");
    }

    @Test
    void shouldBuildSuccessfully_whenAllRequiredFieldsProvided() {
        Order order = validBuilder.build();
        assertNotNull(order);
    }

    @Test
    void shouldSetAllMandatoryFields_onBuiltOrder() {
        Order order = validBuilder.build();
        assertEquals("order-001", order.getOrderId());
        assertEquals("cust-42", order.getCustomerId());
        assertEquals("123 Main St, London", order.getDeliveryAddress());
        assertEquals("CREDIT_CARD", order.getPaymentMethod());
    }

    @Test
    void shouldAllowNullOptionalFields_whenNotSet() {
        Order order = validBuilder.build();
        assertNull(order.getDiscountCode());
        assertNull(order.getPriority());
        assertNull(order.getNotes());
    }

    @Test
    void shouldSetOptionalFields_whenProvided() {
        Order order = validBuilder
                .discountCode("SUMMER20")
                .priority("EXPRESS")
                .notes("Leave at door")
                .build();
        assertEquals("SUMMER20", order.getDiscountCode());
        assertEquals("EXPRESS", order.getPriority());
        assertEquals("Leave at door", order.getNotes());
    }

    @Test
    void shouldReturnBuilderInstance_forFluentChaining() {
        Order.Builder builder = new Order.Builder();
        Order.Builder result = builder.customerId("cust-1");
        assertSame(builder, result);
    }

    @Test
    void shouldThrowIllegalStateException_whenNoItems() {
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .deliveryAddress("123 Main St")
                .paymentMethod("CREDIT_CARD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("no items"),
                "Message should mention 'no items' but was: " + ex.getMessage());
    }

    @Test
    void shouldThrowIllegalStateException_whenNoCustomerId() {
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .items(validItems)
                .deliveryAddress("123 Main St")
                .paymentMethod("CREDIT_CARD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("customer"),
                "Message should mention 'customer' but was: " + ex.getMessage());
    }

    @Test
    void shouldThrowIllegalStateException_whenNoDeliveryAddress() {
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .items(validItems)
                .paymentMethod("CREDIT_CARD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("delivery address"),
                "Message should mention 'delivery address' but was: " + ex.getMessage());
    }

    @Test
    void shouldThrowIllegalStateException_whenPaymentMethodIsNull() {
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .items(validItems)
                .deliveryAddress("123 Main St");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("payment method"),
                "Message should mention 'payment method' but was: " + ex.getMessage());
    }

    @Test
    void shouldThrowIllegalStateException_whenItemQuantityIsZero() {
        List<OrderItem> badItems = List.of(new OrderItem("prod-1", 0, new BigDecimal("9.99")));
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .items(badItems)
                .deliveryAddress("123 Main St")
                .paymentMethod("CREDIT_CARD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("quantity"),
                "Message should mention 'quantity' but was: " + ex.getMessage());
    }

    @Test
    void shouldThrowIllegalStateException_whenItemPriceIsZero() {
        List<OrderItem> badItems = List.of(new OrderItem("prod-1", 1, BigDecimal.ZERO));
        Order.Builder builder = new Order.Builder()
                .orderId("order-001")
                .customerId("cust-42")
                .items(badItems)
                .deliveryAddress("123 Main St")
                .paymentMethod("CREDIT_CARD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().toLowerCase().contains("price"),
                "Message should mention 'price' but was: " + ex.getMessage());
    }

    @Test
    void shouldCollectMultipleValidationErrors_inOneException() {
        Order.Builder builder = new Order.Builder();

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("no items") || msg.contains("customer") || msg.contains("delivery address"),
                "Message should contain multiple errors but was: " + ex.getMessage());
    }

    @Test
    void shouldContainAllItems_onBuiltOrder() {
        Order order = validBuilder.build();
        assertEquals(2, order.getItems().size());
        assertEquals("prod-1", order.getItems().get(0).getProductId());
    }
}
