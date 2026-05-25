package com.practice.model;

import java.time.DayOfWeek;
import java.util.Set;

public class BusinessHours {
    private final int startHour;
    private final int endHour;
    private final Set<DayOfWeek> workingDays;

    public BusinessHours(int startHour, int endHour, Set<DayOfWeek> workingDays) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.workingDays = workingDays;
    }

    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public Set<DayOfWeek> getWorkingDays() { return workingDays; }
}
