package com.port.solver.model;

/**
 * A zone on the quay that is restricted during certain shifts (e.g. maintenance).
 */
public class ForbiddenZone {
    private int startBerthPosition;
    private int endBerthPosition;
    private int startShift;
    private int endShift;  // Exclusive
    private String description;

    public ForbiddenZone() {}

    public ForbiddenZone(int startBerthPosition, int endBerthPosition,
                         int startShift, int endShift, String description) {
        this.startBerthPosition = startBerthPosition;
        this.endBerthPosition = endBerthPosition;
        this.startShift = startShift;
        this.endShift = endShift;
        this.description = description;
    }

    public int getStartBerthPosition() { return startBerthPosition; }
    public void setStartBerthPosition(int startBerthPosition) { this.startBerthPosition = startBerthPosition; }
    public int getEndBerthPosition() { return endBerthPosition; }
    public void setEndBerthPosition(int endBerthPosition) { this.endBerthPosition = endBerthPosition; }
    public int getStartShift() { return startShift; }
    public void setStartShift(int startShift) { this.startShift = startShift; }
    public int getEndShift() { return endShift; }
    public void setEndShift(int endShift) { this.endShift = endShift; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
