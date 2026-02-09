package com.port.solver.engine;

import com.port.solver.model.*;

import java.util.List;
import java.util.Map;

/**
 * Text summary printer for BAP+QCAP solutions.
 * Port of print_solution() from visualization.py.
 */
public class SolutionPrinter {

    public static String printSolution(Problem problem, Solution solution) {
        StringBuilder sb = new StringBuilder();

        sb.append("=".repeat(70)).append('\n');
        sb.append(String.format("Solution Status: %s%n", solution.getStatus()));
        sb.append(String.format("Objective Value: %.2f%n", solution.getObjectiveValue()));
        sb.append("=".repeat(70)).append('\n');

        if (solution.getVesselSolutions().isEmpty()) {
            sb.append("No feasible solution found.\n");
            return sb.toString();
        }

        Map<String, Vessel> vesselsByName = new java.util.HashMap<>();
        for (Vessel v : problem.getVessels()) {
            vesselsByName.put(v.getName(), v);
        }

        Map<String, Crane> craneMap = new java.util.HashMap<>();
        for (Crane c : problem.getCranes()) {
            craneMap.put(c.getId(), c);
        }

        for (VesselSolution vs : solution.getVesselSolutions()) {
            Vessel vessel = vesselsByName.get(vs.getVesselName());
            ProductivityMode pref = vessel.getProductivityPreference();

            int totalMoves = 0;
            for (Map.Entry<Integer, List<String>> entry : vs.getAssignedCranes().entrySet()) {
                for (String cid : entry.getValue()) {
                    Crane c = craneMap.get(cid);
                    if (c != null) {
                        int prod = getProductivity(c, pref);
                        totalMoves += prod;
                    }
                }
            }

            sb.append(String.format("%n--- %s ---%n", vs.getVesselName()));
            sb.append(String.format("  Berth position: %dm - %dm%n",
                    vs.getBerthPosition(), vs.getBerthPosition() + vessel.getLoa()));
            sb.append(String.format("  Time: shift %d -> %d (duration: %d shifts)%n",
                    vs.getStartShift(), vs.getEndShift(), vs.getEndShift() - vs.getStartShift()));

            String deadline = vessel.getDepartureDeadline() != null ?
                    vessel.getDepartureDeadline().toString() : "Not Set (Auto)";

            String completionDt = "Unknown";
            if (vs.getEndShift() > 0 && vs.getEndShift() <= problem.getShifts().size()) {
                completionDt = problem.getShifts().get(vs.getEndShift() - 1).getEndTime().toString();
            } else if (vs.getEndShift() > problem.getShifts().size()) {
                int extra = vs.getEndShift() - problem.getShifts().size();
                completionDt = problem.getShifts().get(problem.getShifts().size() - 1).getEndTime()
                        + " (+" + extra + " shifts)";
            }

            sb.append(String.format("  Arrival: %s, Deadline: %s%n", vessel.getArrivalTime(), deadline));
            sb.append(String.format("  Calculated ETC: %s%n", completionDt));
            sb.append(String.format("  Internal: Shift %d (Fraction: %.2f)%n",
                    vessel.getArrivalShiftIndex(), vessel.getArrivalFraction()));
            sb.append(String.format("  Productivity Mode: %s%n", pref));
            sb.append(String.format("  Workload: %d moves, Capacity delivered: %d moves%n",
                    vessel.getWorkload(), totalMoves));
            sb.append("  Crane assignment per shift:\n");

            for (int t = vs.getStartShift(); t < vs.getEndShift(); t++) {
                List<String> craneIds = vs.getAssignedCranes().getOrDefault(t, List.of());
                int movesThisShift = 0;
                for (String cid : craneIds) {
                    Crane c = craneMap.get(cid);
                    if (c != null) {
                        movesThisShift += getProductivity(c, pref);
                    }
                }
                sb.append(String.format("    Shift %d: %d cranes %s (%d moves)%n",
                        t, craneIds.size(), craneIds, movesThisShift));
            }
        }

        // Global crane usage summary
        sb.append('\n').append("=".repeat(70)).append('\n');
        sb.append("Global Crane Usage per Shift:\n");
        for (int t = 0; t < problem.getNumShifts(); t++) {
            int usedCount = 0;
            for (VesselSolution vs : solution.getVesselSolutions()) {
                usedCount += vs.getAssignedCranes().getOrDefault(t, List.of()).size();
            }
            int availableCount = problem.getCraneAvailabilityPerShift().getOrDefault(t, List.of()).size();
            String bar = "#".repeat(Math.min(20, usedCount));
            sb.append(String.format("  Shift %d: %d/%d [%s]%n", t, usedCount, availableCount, bar));
        }

        return sb.toString();
    }

    private static int getProductivity(Crane c, ProductivityMode pref) {
        return switch (pref) {
            case MAX -> c.getMaxProductivity();
            case MIN -> c.getMinProductivity();
            case INTERMEDIATE -> (c.getMinProductivity() + c.getMaxProductivity()) / 2;
        };
    }
}
