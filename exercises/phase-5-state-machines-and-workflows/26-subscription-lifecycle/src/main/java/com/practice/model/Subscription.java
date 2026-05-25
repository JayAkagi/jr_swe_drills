package com.practice.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Subscription {

    private final String subscriptionId;
    private final String customerId;
    private final String plan;
    private SubscriptionStatus status;
    private final LocalDate startDate;
    private final LocalDate trialEndDate;
    private LocalDate renewalDate;
    private LocalDate cancellationDate;
    private List<SubscriptionEvent> events;

    public Subscription(String subscriptionId, String customerId, String plan,
                        LocalDate startDate, LocalDate trialEndDate) {
        this.subscriptionId = subscriptionId;
        this.customerId = customerId;
        this.plan = plan;
        this.status = SubscriptionStatus.TRIAL;
        this.startDate = startDate;
        this.trialEndDate = trialEndDate;
        this.renewalDate = null;
        this.cancellationDate = null;
        this.events = new ArrayList<>();
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPlan() {
        return plan;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getTrialEndDate() {
        return trialEndDate;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDate cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public List<SubscriptionEvent> getEvents() {
        return events;
    }
}
