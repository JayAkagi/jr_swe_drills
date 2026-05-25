package com.practice.service;

import com.practice.model.Order;
import com.practice.model.OrderStatus;
import com.practice.model.StatusChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderStateMachineTest {

    private OrderStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = new OrderStateMachine();
    }

    @Test
    void shouldTransition_whenPendingToConfirmed() {
        Order order = new Order("ORD-001");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Customer confirmed");
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void shouldTransition_whenConfirmedToProcessing() {
        Order order = new Order("ORD-002");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Started processing");
        assertEquals(OrderStatus.PROCESSING, order.getStatus());
    }

    @Test
    void shouldTransition_whenProcessingToShipped() {
        Order order = new Order("ORD-003");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Processing");
        stateMachine.transition(order, OrderStatus.SHIPPED, "Dispatched");
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shouldTransition_whenShippedToDelivered() {
        Order order = new Order("ORD-004");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Processing");
        stateMachine.transition(order, OrderStatus.SHIPPED, "Shipped");
        stateMachine.transition(order, OrderStatus.DELIVERED, "Delivered to door");
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void shouldTransition_whenDeliveredToReturned() {
        Order order = new Order("ORD-005");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Processing");
        stateMachine.transition(order, OrderStatus.SHIPPED, "Shipped");
        stateMachine.transition(order, OrderStatus.DELIVERED, "Delivered");
        stateMachine.transition(order, OrderStatus.RETURNED, "Customer return");
        assertEquals(OrderStatus.RETURNED, order.getStatus());
    }

    @Test
    void shouldTransition_whenPendingToCancelled() {
        Order order = new Order("ORD-006");
        stateMachine.transition(order, OrderStatus.CANCELLED, "Customer cancelled");
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldTransition_whenConfirmedToCancelled() {
        Order order = new Order("ORD-007");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.CANCELLED, "Cancelled after confirmation");
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldThrowInvalidTransitionException_whenPendingToShipped() {
        Order order = new Order("ORD-008");
        assertThrows(InvalidTransitionException.class,
                () -> stateMachine.transition(order, OrderStatus.SHIPPED, "Skipping steps"));
    }

    @Test
    void shouldThrowInvalidTransitionException_whenDeliveredToCancelled() {
        Order order = new Order("ORD-009");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Processing");
        stateMachine.transition(order, OrderStatus.SHIPPED, "Shipped");
        stateMachine.transition(order, OrderStatus.DELIVERED, "Delivered");
        assertThrows(InvalidTransitionException.class,
                () -> stateMachine.transition(order, OrderStatus.CANCELLED, "Too late to cancel"));
    }

    @Test
    void shouldIncludeBothStateNames_whenExceptionThrown() {
        Order order = new Order("ORD-010");
        InvalidTransitionException ex = assertThrows(InvalidTransitionException.class,
                () -> stateMachine.transition(order, OrderStatus.SHIPPED, "Skipping"));
        String message = ex.getMessage();
        assertTrue(message.contains("PENDING"), "Message should contain source state PENDING");
        assertTrue(message.contains("SHIPPED"), "Message should contain target state SHIPPED");
    }

    @Test
    void shouldMaintainFullHistory_acrossMultipleTransitions() {
        Order order = new Order("ORD-011");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        stateMachine.transition(order, OrderStatus.PROCESSING, "Processing");
        stateMachine.transition(order, OrderStatus.SHIPPED, "Shipped");
        assertEquals(3, order.getHistory().size());
    }

    @Test
    void shouldRecordCorrectFromAndTo_inHistory() {
        Order order = new Order("ORD-012");
        stateMachine.transition(order, OrderStatus.CONFIRMED, "Confirmed");
        StatusChange change = order.getHistory().get(0);
        assertEquals(OrderStatus.PENDING, change.getFrom());
        assertEquals(OrderStatus.CONFIRMED, change.getTo());
    }

    @Test
    void shouldStoreReason_inHistoryEntry() {
        Order order = new Order("ORD-013");
        String reason = "Manager approved";
        stateMachine.transition(order, OrderStatus.CONFIRMED, reason);
        assertEquals(reason, order.getHistory().get(0).getReason());
    }
}
