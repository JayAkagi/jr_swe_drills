package com.practice.service;

import com.practice.model.Event;
import com.practice.model.OrderPlacedEvent;
import com.practice.model.PaymentReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    private EventBus eventBus;
    private OrderPlacedEvent orderPlacedEvent;
    private PaymentReceivedEvent paymentReceivedEvent;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
        orderPlacedEvent = new OrderPlacedEvent("order-1", "cust-1", new BigDecimal("99.99"));
        paymentReceivedEvent = new PaymentReceivedEvent("order-1", new BigDecimal("99.99"), "CARD");
    }

    @Test
    void shouldCallListener_whenSubscribedEventTypeIsPublished() {
        AtomicInteger callCount = new AtomicInteger(0);
        eventBus.subscribe(OrderPlacedEvent.class, e -> callCount.incrementAndGet());

        eventBus.publish(orderPlacedEvent);

        assertEquals(1, callCount.get());
    }

    @Test
    void shouldNotCallListener_whenDifferentEventTypeIsPublished() {
        AtomicInteger callCount = new AtomicInteger(0);
        eventBus.subscribe(OrderPlacedEvent.class, e -> callCount.incrementAndGet());

        eventBus.publish(paymentReceivedEvent);

        assertEquals(0, callCount.get());
    }

    @Test
    void shouldCallBothListeners_whenTwoSubscribersForSameType() {
        AtomicInteger count1 = new AtomicInteger(0);
        AtomicInteger count2 = new AtomicInteger(0);
        eventBus.subscribe(OrderPlacedEvent.class, e -> count1.incrementAndGet());
        eventBus.subscribe(OrderPlacedEvent.class, e -> count2.incrementAndGet());

        eventBus.publish(orderPlacedEvent);

        assertEquals(1, count1.get());
        assertEquals(1, count2.get());
    }

    @Test
    void shouldNotThrow_whenNoSubscribersForPublishedEvent() {
        assertDoesNotThrow(() -> eventBus.publish(orderPlacedEvent));
    }

    @Test
    void shouldNotStopOtherListeners_whenOneListenerThrows() {
        AtomicInteger count = new AtomicInteger(0);
        eventBus.subscribe(OrderPlacedEvent.class, e -> {
            throw new RuntimeException("Listener failed");
        });
        eventBus.subscribe(OrderPlacedEvent.class, e -> count.incrementAndGet());

        assertDoesNotThrow(() -> eventBus.publish(orderPlacedEvent));
        assertEquals(1, count.get());
    }

    @Test
    void shouldReceiveExactEventObject_inListener() {
        List<OrderPlacedEvent> received = new ArrayList<>();
        eventBus.subscribe(OrderPlacedEvent.class, received::add);

        eventBus.publish(orderPlacedEvent);

        assertSame(orderPlacedEvent, received.get(0));
    }

    @Test
    void shouldRouteCorrectly_whenMultipleEventTypesSubscribed() {
        List<Event> orderEvents = new ArrayList<>();
        List<Event> paymentEvents = new ArrayList<>();
        eventBus.subscribe(OrderPlacedEvent.class, orderEvents::add);
        eventBus.subscribe(PaymentReceivedEvent.class, paymentEvents::add);

        eventBus.publish(orderPlacedEvent);
        eventBus.publish(paymentReceivedEvent);

        assertEquals(1, orderEvents.size());
        assertEquals(1, paymentEvents.size());
    }

    @Test
    void shouldNotDeliverOrderPlacedEvent_toPaymentListener() {
        AtomicInteger paymentListenerCount = new AtomicInteger(0);
        eventBus.subscribe(PaymentReceivedEvent.class, e -> paymentListenerCount.incrementAndGet());

        eventBus.publish(orderPlacedEvent);

        assertEquals(0, paymentListenerCount.get());
    }

    @Test
    void shouldAllowAccessToEventFields_inListener() {
        List<String> capturedOrderIds = new ArrayList<>();
        eventBus.subscribe(OrderPlacedEvent.class, e -> capturedOrderIds.add(e.getOrderId()));

        eventBus.publish(orderPlacedEvent);

        assertEquals("order-1", capturedOrderIds.get(0));
    }

    @Test
    void shouldDeliverPaymentEvent_toPaymentListener() {
        List<PaymentReceivedEvent> received = new ArrayList<>();
        eventBus.subscribe(PaymentReceivedEvent.class, received::add);

        eventBus.publish(paymentReceivedEvent);

        assertEquals(1, received.size());
        assertSame(paymentReceivedEvent, received.get(0));
    }

    @Test
    void shouldHandleMultiplePublishes_forSameEventType() {
        AtomicInteger count = new AtomicInteger(0);
        eventBus.subscribe(OrderPlacedEvent.class, e -> count.incrementAndGet());

        eventBus.publish(orderPlacedEvent);
        eventBus.publish(new OrderPlacedEvent("order-2", "cust-2", new BigDecimal("50.00")));

        assertEquals(2, count.get());
    }

    @Test
    void shouldAllowAccessToBaseEventFields_inListener() {
        List<String> eventTypes = new ArrayList<>();
        eventBus.subscribe(OrderPlacedEvent.class, e -> eventTypes.add(e.getEventType()));

        eventBus.publish(orderPlacedEvent);

        assertEquals("ORDER_PLACED", eventTypes.get(0));
    }
}
