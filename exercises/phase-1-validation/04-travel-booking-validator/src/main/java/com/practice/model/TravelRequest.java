package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class TravelRequest {

    private final String employeeName;
    private final BigDecimal budgetGBP;
    private final List<Booking> bookings;

    public TravelRequest(String employeeName, BigDecimal budgetGBP, List<Booking> bookings) {
        this.employeeName = employeeName;
        this.budgetGBP = budgetGBP;
        this.bookings = bookings;
    }

    public String getEmployeeName() { return employeeName; }
    public BigDecimal getBudgetGBP() { return budgetGBP; }
    public List<Booking> getBookings() { return bookings; }
}
