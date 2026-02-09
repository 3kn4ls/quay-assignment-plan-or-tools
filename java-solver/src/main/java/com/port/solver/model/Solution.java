package com.port.solver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete solution for the BAP + QCAP problem.
 */
public class Solution {
    private List<VesselSolution> vesselSolutions;
    private double objectiveValue;
    private String status;

    public Solution() {
        this.vesselSolutions = new ArrayList<>();
    }

    public Solution(List<VesselSolution> vesselSolutions, double objectiveValue, String status) {
        this.vesselSolutions = vesselSolutions != null ? vesselSolutions : new ArrayList<>();
        this.objectiveValue = objectiveValue;
        this.status = status;
    }

    public List<VesselSolution> getVesselSolutions() { return vesselSolutions; }
    public void setVesselSolutions(List<VesselSolution> vesselSolutions) { this.vesselSolutions = vesselSolutions; }
    public double getObjectiveValue() { return objectiveValue; }
    public void setObjectiveValue(double objectiveValue) { this.objectiveValue = objectiveValue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
