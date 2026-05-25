package com.practice.service;

import com.practice.model.DeliveryResult;
import com.practice.model.Notification;
import com.practice.model.NotificationChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDispatcherTest {

    private Notification notification;
    private NotificationSender emailSender;
    private NotificationSender smsSender;
    private NotificationSender pushSender;
    private NotificationDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        notification = new Notification("user-1", "Hello", "Body text", "HIGH");

        emailSender = new EmailSender();
        smsSender = new SmsSender();
        pushSender = new PushSender();

        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.EMAIL, emailSender);
        senders.put(NotificationChannel.SMS, smsSender);
        senders.put(NotificationChannel.PUSH, pushSender);

        dispatcher = new NotificationDispatcher(senders);
    }

    @Test
    void shouldCallEmailSender_whenEmailChannelRequested() {
        AtomicBoolean called = new AtomicBoolean(false);
        NotificationSender trackingEmail = n -> {
            called.set(true);
            return new DeliveryResult(n.getRecipientId(), NotificationChannel.EMAIL, true, null);
        };
        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.EMAIL, trackingEmail);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        d.dispatch(notification, List.of(NotificationChannel.EMAIL));

        assertTrue(called.get());
    }

    @Test
    void shouldCallSmsSender_whenSmsChannelRequested() {
        AtomicBoolean called = new AtomicBoolean(false);
        NotificationSender trackingSms = n -> {
            called.set(true);
            return new DeliveryResult(n.getRecipientId(), NotificationChannel.SMS, true, null);
        };
        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.SMS, trackingSms);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        d.dispatch(notification, List.of(NotificationChannel.SMS));

        assertTrue(called.get());
    }

    @Test
    void shouldCallPushSender_whenPushChannelRequested() {
        AtomicBoolean called = new AtomicBoolean(false);
        NotificationSender trackingPush = n -> {
            called.set(true);
            return new DeliveryResult(n.getRecipientId(), NotificationChannel.PUSH, true, null);
        };
        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.PUSH, trackingPush);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        d.dispatch(notification, List.of(NotificationChannel.PUSH));

        assertTrue(called.get());
    }

    @Test
    void shouldReturnThreeResults_whenAllThreeChannelsRequested() {
        List<DeliveryResult> results = dispatcher.dispatch(
                notification, List.of(NotificationChannel.EMAIL, NotificationChannel.SMS, NotificationChannel.PUSH));

        assertEquals(3, results.size());
    }

    @Test
    void shouldReturnOneResult_whenSubsetOfChannelsRequested() {
        List<DeliveryResult> results = dispatcher.dispatch(
                notification, List.of(NotificationChannel.EMAIL));

        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyList_whenNoChannelsRequested() {
        List<DeliveryResult> results = dispatcher.dispatch(notification, List.of());

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldHaveCorrectRecipientId_inResult() {
        List<DeliveryResult> results = dispatcher.dispatch(
                notification, List.of(NotificationChannel.EMAIL));

        assertEquals("user-1", results.get(0).getRecipientId());
    }

    @Test
    void shouldHaveCorrectChannel_inResult() {
        List<DeliveryResult> results = dispatcher.dispatch(
                notification, List.of(NotificationChannel.SMS));

        assertEquals(NotificationChannel.SMS, results.get(0).getChannel());
    }

    @Test
    void shouldReturnSuccessTrue_forWorkingSenders() {
        List<DeliveryResult> results = dispatcher.dispatch(
                notification, List.of(NotificationChannel.EMAIL));

        assertTrue(results.get(0).isSuccess());
    }

    @Test
    void shouldNotBlockOtherChannels_whenOneSenderThrows() {
        NotificationSender failingSender = n -> {
            throw new RuntimeException("Connection refused");
        };
        NotificationSender workingSender = n ->
                new DeliveryResult(n.getRecipientId(), NotificationChannel.SMS, true, null);

        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.EMAIL, failingSender);
        senders.put(NotificationChannel.SMS, workingSender);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        List<DeliveryResult> results = d.dispatch(
                notification, List.of(NotificationChannel.EMAIL, NotificationChannel.SMS));

        assertEquals(2, results.size());
    }

    @Test
    void shouldSetSuccessFalse_whenSenderThrows() {
        NotificationSender failingSender = n -> {
            throw new RuntimeException("Connection refused");
        };
        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.EMAIL, failingSender);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        List<DeliveryResult> results = d.dispatch(notification, List.of(NotificationChannel.EMAIL));

        assertFalse(results.get(0).isSuccess());
    }

    @Test
    void shouldSetErrorMessage_whenSenderThrows() {
        NotificationSender failingSender = n -> {
            throw new RuntimeException("Connection refused");
        };
        Map<NotificationChannel, NotificationSender> senders = new HashMap<>();
        senders.put(NotificationChannel.EMAIL, failingSender);
        NotificationDispatcher d = new NotificationDispatcher(senders);

        List<DeliveryResult> results = d.dispatch(notification, List.of(NotificationChannel.EMAIL));

        assertEquals("Connection refused", results.get(0).getErrorMessage());
    }
}
