package com.port.solver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Full problem instance for BAP + QCAP optimization.
 */
public class Problem {
    private Berth berth;
    private List<Vessel> vessels;
    private List<Crane> cranes;
    private List<Shift> shifts;
    private Map<Integer, List<String>> craneAvailabilityPerShift;
    private List<ForbiddenZone> forbiddenZones;
    private List<YardQuayZone> yardQuayZones;
    private Map<String, Boolean> solverRules;

    public Problem() {
        this.vessels = new ArrayList<>();
        this.cranes = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.craneAvailabilityPerShift = new HashMap<>();
        this.forbiddenZones = new ArrayList<>();
        this.yardQuayZones = new ArrayList<>();
        this.solverRules = new HashMap<>();
    }

    public Problem(Berth berth, List<Vessel> vessels, List<Crane> cranes,
                   List<Shift> shifts, Map<Integer, List<String>> craneAvailabilityPerShift,
                   List<ForbiddenZone> forbiddenZones, List<YardQuayZone> yardQuayZones,
                   Map<String, Boolean> solverRules) {
        this.berth = berth;
        this.vessels = vessels;
        this.cranes = cranes;
        this.shifts = shifts;
        this.craneAvailabilityPerShift = craneAvailabilityPerShift;
        this.forbiddenZones = forbiddenZones != null ? forbiddenZones : new ArrayList<>();
        this.yardQuayZones = yardQuayZones != null ? yardQuayZones : new ArrayList<>();
        this.solverRules = solverRules != null ? solverRules : new HashMap<>();
    }

    public int getNumShifts() {
        return shifts.size();
    }

    public boolean getSolverRule(String key, boolean defaultValue) {
        return solverRules.getOrDefault(key, defaultValue);
    }

    // Getters and setters
    public Berth getBerth() { return berth; }
    public void setBerth(Berth berth) { this.berth = berth; }
    public List<Vessel> getVessels() { return vessels; }
    public void setVessels(List<Vessel> vessels) { this.vessels = vessels; }
    public List<Crane> getCranes() { return cranes; }
    public void setCranes(List<Crane> cranes) { this.cranes = cranes; }
    public List<Shift> getShifts() { return shifts; }
    public void setShifts(List<Shift> shifts) { this.shifts = shifts; }
    public Map<Integer, List<String>> getCraneAvailabilityPerShift() { return craneAvailabilityPerShift; }
    public void setCraneAvailabilityPerShift(Map<Integer, List<String>> m) { this.craneAvailabilityPerShift = m; }
    public List<ForbiddenZone> getForbiddenZones() { return forbiddenZones; }
    public void setForbiddenZones(List<ForbiddenZone> forbiddenZones) { this.forbiddenZones = forbiddenZones; }
    public List<YardQuayZone> getYardQuayZones() { return yardQuayZones; }
    public void setYardQuayZones(List<YardQuayZone> yardQuayZones) { this.yardQuayZones = yardQuayZones; }
    public Map<String, Boolean> getSolverRules() { return solverRules; }
    public void setSolverRules(Map<String, Boolean> solverRules) { this.solverRules = solverRules; }
}
