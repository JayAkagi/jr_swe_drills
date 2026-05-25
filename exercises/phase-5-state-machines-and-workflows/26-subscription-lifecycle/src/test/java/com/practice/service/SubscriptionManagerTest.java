package com.practice.service;

import com.practice.model.Subscription;
import com.practice.model.SubscriptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionManagerTest {

    private SubscriptionManager manager;
    private LocalDate startDate;
    private LocalDate trialEndDate;

    @BeforeEach
    void setUp() {
        manager = new SubscriptionManager();
        startDate = LocalDate.of(2024, 1, 1);
        trialEndDate = LocalDate.of(2024, 1, 15);
    }

    private Subscription newSubscription() {
        return new Subscription("SUB-001", "CUST-001", "BASIC", startDate, trialEndDate);
    }

    @Test
    void shouldActivate_whenTrialHasEnded() {
        Subscription s = newSubscription();
        LocalDate today = trialEndDate;
        manager.activate(s, today);
        assertEquals(SubscriptionStatus.ACTIVE, s.getStatus());
    }

    @Test
    void shouldThrowIllegalStateException_whenActivatingBeforeTrialEnds() {
        Subscription s = newSubscription();
        LocalDate today = trialEndDate.minusDays(1);
        assertThrows(IllegalStateException.class, () -> manager.activate(s, today));
    }

    @Test
    void shouldMoveToPassDue_whenRenewalFailsOnActive() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        assertEquals(SubscriptionStatus.PAST_DUE, s.getStatus());
    }

    @Test
    void shouldThrowIllegalStateException_whenRenewalFailedOnNonActive() {
        Subscription s = newSubscription();
        assertThrows(IllegalStateException.class,
                () -> manager.renewalFailed(s, trialEndDate.plusDays(1)));
    }

    @Test
    void shouldSuspend_whenMoreThan7DaysPastDue() {
        Subscription s = newSubscription();
        LocalDate activationDate = trialEndDate;
        manager.activate(s, activationDate);
        LocalDate failDate = activationDate.plusDays(1);
        manager.renewalFailed(s, failDate);
        LocalDate suspendDate = s.getRenewalDate().plusDays(8);
        manager.suspend(s, suspendDate);
        assertEquals(SubscriptionStatus.SUSPENDED, s.getStatus());
    }

    @Test
    void shouldThrowIllegalStateException_whenSuspendingWithin7DayGracePeriod() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        LocalDate suspendDate = s.getRenewalDate().plusDays(5);
        assertThrows(IllegalStateException.class, () -> manager.suspend(s, suspendDate));
    }

    @Test
    void shouldCancel_whenStatusIsTrial() {
        Subscription s = newSubscription();
        manager.cancel(s, trialEndDate.minusDays(3));
        assertEquals(SubscriptionStatus.CANCELLED, s.getStatus());
    }

    @Test
    void shouldCancel_whenStatusIsActive() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.cancel(s, trialEndDate.plusDays(5));
        assertEquals(SubscriptionStatus.CANCELLED, s.getStatus());
    }

    @Test
    void shouldCancel_whenStatusIsSuspended() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        LocalDate suspendDate = s.getRenewalDate().plusDays(8);
        manager.suspend(s, suspendDate);
        manager.cancel(s, suspendDate.plusDays(1));
        assertEquals(SubscriptionStatus.CANCELLED, s.getStatus());
    }

    @Test
    void shouldSetCancellationDate_whenCancelled() {
        Subscription s = newSubscription();
        LocalDate today = LocalDate.of(2024, 2, 10);
        manager.cancel(s, today);
        assertEquals(today, s.getCancellationDate());
    }

    @Test
    void shouldReactivate_whenStatusIsPastDue() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        LocalDate reactivateDate = trialEndDate.plusDays(10);
        manager.reactivate(s, reactivateDate);
        assertEquals(SubscriptionStatus.ACTIVE, s.getStatus());
    }

    @Test
    void shouldReactivate_whenStatusIsSuspended() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        LocalDate suspendDate = s.getRenewalDate().plusDays(8);
        manager.suspend(s, suspendDate);
        manager.reactivate(s, suspendDate.plusDays(2));
        assertEquals(SubscriptionStatus.ACTIVE, s.getStatus());
    }

    @Test
    void shouldRecordEventsInHistory_forEachTransition() {
        Subscription s = newSubscription();
        manager.activate(s, trialEndDate);
        manager.renewalFailed(s, trialEndDate.plusDays(1));
        assertEquals(2, s.getEvents().size());
    }

    @Test
    void shouldUpdateRenewalDate_whenActivatedOrReactivated() {
        Subscription s = newSubscription();
        LocalDate activationDate = trialEndDate;
        manager.activate(s, activationDate);
        assertEquals(activationDate.plusDays(30), s.getRenewalDate());

        manager.renewalFailed(s, activationDate.plusDays(1));
        LocalDate reactivateDate = activationDate.plusDays(10);
        manager.reactivate(s, reactivateDate);
        assertEquals(reactivateDate.plusDays(30), s.getRenewalDate());
    }
}
