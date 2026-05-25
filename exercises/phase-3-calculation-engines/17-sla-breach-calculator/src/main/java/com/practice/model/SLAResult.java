package com.practice.model;

public class SLAResult {
    private final String ticketId;
    private final Priority priority;
    private final int slaTargetHours;
    private final double actualBusinessHours;
    private final boolean breached;
    private final double breachByHours;

    public SLAResult(String ticketId, Priority priority, int slaTargetHours,
                     double actualBusinessHours, boolean breached, double breachByHours) {
        this.ticketId = ticketId;
        this.priority = priority;
        this.slaTargetHours = slaTargetHours;
        this.actualBusinessHours = actualBusinessHours;
        this.breached = breached;
        this.breachByHours = breachByHours;
    }

    public String getTicketId() { return ticketId; }
    public Priority getPriority() { return priority; }
    public int getSlaTargetHours() { return slaTargetHours; }
    public double getActualBusinessHours() { return actualBusinessHours; }
    public boolean isBreached() { return breached; }
    public double getBreachByHours() { return breachByHours; }
}
