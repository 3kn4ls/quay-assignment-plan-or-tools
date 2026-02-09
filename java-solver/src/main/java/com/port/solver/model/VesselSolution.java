package com.port.solver.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Solution for a single vessel.
 */
public class VesselSolution {
    private String vesselName;
    private int berthPosition;
    private int startShift;
    private int endShift;  // Exclusive
    private Map<Integer, List<String>> assignedCranes;

    public VesselSolution() {
        this.assignedCranes = new HashMap<>();
    }

    public VesselSolution(String vesselName, int berthPosition,
                          int startShift, int endShift,
                          Map<Integer, List<String>> assignedCranes) {
        this.vesselName = vesselName;
        this.berthPosition = berthPosition;
        this.startShift = startShift;
        this.endShift = endShift;
        this.assignedCranes = assignedCranes != null ? assignedCranes : new HashMap<>();
    }

    public String getVesselName() { return vesselName; }
    public void setVesselName(String vesselName) { this.vesselName = vesselName; }
    public int getBerthPosition() { return berthPosition; }
    public void setBerthPosition(int berthPosition) { this.berthPosition = berthPosition; }
    public int getStartShift() { return startShift; }
    public void setStartShift(int startShift) { this.startShift = startShift; }
    public int getEndShift() { return endShift; }
    public void setEndShift(int endShift) { this.endShift = endShift; }
    public Map<Integer, List<String>> getAssignedCranes() { return assignedCranes; }
    public void setAssignedCranes(Map<Integer, List<String>> assignedCranes) { this.assignedCranes = assignedCranes; }
}
