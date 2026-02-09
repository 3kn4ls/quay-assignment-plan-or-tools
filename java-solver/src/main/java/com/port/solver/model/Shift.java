package com.port.solver.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Represents a specific operational shift (6-hour block).
 */
public class Shift {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Shift() {}

    public Shift(int id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getDurationHours() {
        return Duration.between(startTime, endTime).toSeconds() / 3600.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return startTime.format(DateTimeFormatter.ofPattern("ddMMyyyy-HHmm"));
    }
}
