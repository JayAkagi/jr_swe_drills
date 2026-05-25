package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {

    private final String destination;
    private final LocalDate departureDate;
    private final LocalDate returnDate;
    private final TravelClass travelClass;
    private final BigDecimal costGBP;
    private final BookingType bookingType;
    private final boolean managerApproved;

    public Booking(String destination, LocalDate departureDate, LocalDate returnDate,
                   TravelClass travelClass, BigDecimal costGBP, BookingType bookingType, boolean managerApproved) {
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.travelClass = travelClass;
        this.costGBP = costGBP;
        this.bookingType = bookingType;
        this.managerApproved = managerApproved;
    }

    public String getDestination() { return destination; }
    public LocalDate getDepartureDate() { return departureDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public TravelClass getTravelClass() { return travelClass; }
    public BigDecimal getCostGBP() { return costGBP; }
    public BookingType getBookingType() { return bookingType; }
    public boolean isManagerApproved() { return managerApproved; }
}
