package com.port.solver.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A vessel (ship) that needs berth allocation and crane assignment.
 */
public class Vessel {
    private String name;
    private int workload;          // Total container movements needed
    private int loa;               // Length Over All in meters
    private double draft;          // Required depth for mooring
    private LocalDateTime arrivalTime;     // ETW (Desired arrival)
    private LocalDateTime departureDeadline; // ETC (Desired completion) - optional
    private int maxCranes = 3;
    private ProductivityMode productivityPreference = ProductivityMode.INTERMEDIATE;
    private List<YardQuayZonePreference> targetZones = new ArrayList<>();
    private List<Integer> availableShifts;

    // Computed at runtime
    private int arrivalShiftIndex = -1;
    private double arrivalFraction = 0.0;
    private int departureShiftIndex = -1;

    public Vessel() {}

    public Vessel(String name, int workload, int loa, double draft,
                  LocalDateTime arrivalTime, LocalDateTime departureDeadline,
                  int maxCranes, ProductivityMode productivityPreference,
                  List<YardQuayZonePreference> targetZones) {
        this.name = name;
        this.workload = workload;
        this.loa = loa;
        this.draft = draft;
        this.arrivalTime = arrivalTime;
        this.departureDeadline = departureDeadline;
        this.maxCranes = maxCranes;
        this.productivityPreference = productivityPreference;
        this.targetZones = targetZones != null ? targetZones : new ArrayList<>();
    }

    public Vessel(String name, int workload, int loa, double draft,
                  LocalDateTime arrivalTime, LocalDateTime departureDeadline) {
        this(name, workload, loa, draft, arrivalTime, departureDeadline,
             3, ProductivityMode.INTERMEDIATE, null);
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getWorkload() { return workload; }
    public void setWorkload(int workload) { this.workload = workload; }
    public int getLoa() { return loa; }
    public void setLoa(int loa) { this.loa = loa; }
    public double getDraft() { return draft; }
    public void setDraft(double draft) { this.draft = draft; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public LocalDateTime getDepartureDeadline() { return departureDeadline; }
    public void setDepartureDeadline(LocalDateTime departureDeadline) { this.departureDeadline = departureDeadline; }
    public int getMaxCranes() { return maxCranes; }
    public void setMaxCranes(int maxCranes) { this.maxCranes = maxCranes; }
    public ProductivityMode getProductivityPreference() { return productivityPreference; }
    public void setProductivityPreference(ProductivityMode productivityPreference) { this.productivityPreference = productivityPreference; }
    public List<YardQuayZonePreference> getTargetZones() { return targetZones; }
    public void setTargetZones(List<YardQuayZonePreference> targetZones) { this.targetZones = targetZones; }
    public List<Integer> getAvailableShifts() { return availableShifts; }
    public void setAvailableShifts(List<Integer> availableShifts) { this.availableShifts = availableShifts; }
    public int getArrivalShiftIndex() { return arrivalShiftIndex; }
    public void setArrivalShiftIndex(int arrivalShiftIndex) { this.arrivalShiftIndex = arrivalShiftIndex; }
    public double getArrivalFraction() { return arrivalFraction; }
    public void setArrivalFraction(double arrivalFraction) { this.arrivalFraction = arrivalFraction; }
    public int getDepartureShiftIndex() { return departureShiftIndex; }
    public void setDepartureShiftIndex(int departureShiftIndex) { this.departureShiftIndex = departureShiftIndex; }
}
