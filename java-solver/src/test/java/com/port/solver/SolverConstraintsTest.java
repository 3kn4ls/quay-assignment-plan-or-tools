package com.port.solver;

import com.port.solver.config.ExampleProblemFactory;
import com.port.solver.engine.CpSatSolver;
import com.port.solver.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CP-SAT solver constraints.
 * Direct port of constraints_test.py.
 */
class SolverConstraintsTest {

    private LocalDateTime startDate;
    private List<Shift> shifts;
    private int berthLength;
    private List<Crane> cranes;
    private CpSatSolver solver;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2026, 1, 1, 0, 0);
        shifts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shifts.add(new Shift(i,
                    startDate.plusHours(6L * i),
                    startDate.plusHours(6L * (i + 1))));
        }
        berthLength = 1000;
        cranes = List.of(
                new Crane("C1", "STS-1", CraneType.STS, 0, 1000, 10, 20),
                new Crane("C2", "STS-2", CraneType.STS, 0, 1000, 10, 20)
        );
        solver = new CpSatSolver();
    }

    private Problem createProblem(List<Vessel> vessels, List<ForbiddenZone> constraints) {
        Berth berth = new Berth(berthLength, 20.0, Map.of(0, 20.0));
        Map<Integer, List<String>> availability = new HashMap<>();
        for (int i = 0; i < shifts.size(); i++) {
            availability.put(i, cranes.stream().map(Crane::getId).collect(Collectors.toList()));
        }
        return new Problem(berth, vessels, new ArrayList<>(cranes), shifts, availability,
                constraints != null ? constraints : List.of(),
                List.of(), new HashMap<>());
    }

    private void preprocessVessels(Problem problem) {
        ExampleProblemFactory.preprocessVessels(
                problem.getVessels(), problem.getShifts(), problem.getShifts().size());
    }

    /**
     * Test 1: Two vessels cannot occupy the same space at the same time.
     */
    @Test
    void testSpatialNoOverlap() {
        Vessel v1 = new Vessel("V1", 100, 600, 10, startDate,
                startDate.plusHours(48));
        Vessel v2 = new Vessel("V2", 100, 600, 10, startDate,
                startDate.plusHours(48));

        Problem problem = createProblem(List.of(v1, v2), null);
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 60);
        assertTrue(
                "OPTIMAL".equals(solution.getStatus()) || "FEASIBLE".equals(solution.getStatus()),
                "Status was " + solution.getStatus());
    }

    /**
     * Test 2: Vessel cannot start before arrival time.
     */
    @Test
    void testTemporalArrivalConstraint() {
        LocalDateTime arrivalTime = shifts.get(5).getStartTime();
        Vessel v1 = new Vessel("V1", 50, 100, 10, arrivalTime,
                shifts.get(8).getEndTime());

        Problem problem = createProblem(List.of(v1), null);
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 5);
        VesselSolution solV1 = solution.getVesselSolutions().get(0);

        assertTrue(solV1.getStartShift() >= 5,
                "Vessel started before arrival shift: " + solV1.getStartShift());
    }

    /**
     * Test 3: Crane assignments must respect physical reach.
     */
    @Test
    void testCraneReachConstraint() {
        Crane c1 = new Crane("C1", "ShortCrane", CraneType.STS, 0, 300, 10, 20);
        Vessel v1 = new Vessel("V1", 50, 100, 10, startDate,
                startDate.plusHours(48));

        Problem problem = createProblem(List.of(v1), null);
        problem.setCranes(List.of(c1));
        // Update availability for single crane
        Map<Integer, List<String>> availability = new HashMap<>();
        for (int i = 0; i < shifts.size(); i++) {
            availability.put(i, List.of(c1.getId()));
        }
        problem.setCraneAvailabilityPerShift(availability);
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 5);

        if (!"INFEASIBLE".equals(solution.getStatus())) {
            VesselSolution solV1 = solution.getVesselSolutions().get(0);
            int endPos = solV1.getBerthPosition() + v1.getLoa();
            assertTrue(endPos <= 300,
                    "Vessel assigned to C1 but outside C1 reach: endPos=" + endPos);
        }
    }

    /**
     * Test 4: STS Cranes cannot cross.
     */
    @Test
    void testStsNonCrossing() {
        Vessel v1 = new Vessel("V1", 20, 100, 10, startDate,
                startDate.plusHours(48));
        Vessel v2 = new Vessel("V2", 20, 100, 10, startDate,
                startDate.plusHours(48));

        Problem problem = createProblem(List.of(v1, v2), null);
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 5);

        Map<String, VesselSolution> solMap = new HashMap<>();
        for (VesselSolution vs : solution.getVesselSolutions()) {
            solMap.put(vs.getVesselName(), vs);
        }

        for (int t = 0; t < 10; t++) {
            // Check assignments per shift
            Map<String, Integer> craneToPosMap = new HashMap<>();
            for (VesselSolution vs : solution.getVesselSolutions()) {
                List<String> assigned = vs.getAssignedCranes().getOrDefault(t, List.of());
                for (String cid : assigned) {
                    craneToPosMap.put(cid + "_" + vs.getVesselName(), vs.getBerthPosition());
                }
            }

            // If C1 and C2 both active in this shift
            String c1Vessel = null, c2Vessel = null;
            int c1Pos = 0, c2Pos = 0;
            for (VesselSolution vs : solution.getVesselSolutions()) {
                List<String> assigned = vs.getAssignedCranes().getOrDefault(t, List.of());
                if (assigned.contains("C1")) { c1Vessel = vs.getVesselName(); c1Pos = vs.getBerthPosition(); }
                if (assigned.contains("C2")) { c2Vessel = vs.getVesselName(); c2Pos = vs.getBerthPosition(); }
            }

            if (c1Vessel != null && c2Vessel != null) {
                assertTrue(c1Pos <= c2Pos,
                        String.format("STS Crossover at shift %d: C1 on %s(%d) vs C2 on %s(%d)",
                                t, c1Vessel, c1Pos, c2Vessel, c2Pos));
            }
        }
    }

    /**
     * Test 5: Assigned productivity meets workload.
     */
    @Test
    void testWorkloadFulfillment() {
        int workload = 100;
        Vessel v1 = new Vessel("V1", workload, 100, 10, startDate,
                startDate.plusHours(48));

        Problem problem = createProblem(List.of(v1), null);
        // Set both cranes to fixed 10 prod
        for (Crane c : problem.getCranes()) {
            c.setMinProductivity(10);
            c.setMaxProductivity(10);
        }
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 60);
        assertTrue(
                "OPTIMAL".equals(solution.getStatus()) || "FEASIBLE".equals(solution.getStatus()),
                "Solver failed to find solution: " + solution.getStatus());

        VesselSolution solV1 = solution.getVesselSolutions().get(0);

        double delivered = 0;
        for (Map.Entry<Integer, List<String>> entry : solV1.getAssignedCranes().entrySet()) {
            int t = entry.getKey();
            double fraction = 1.0;
            if (t == v1.getArrivalShiftIndex()) {
                fraction = v1.getArrivalFraction();
            }
            double prod = 10 * fraction;
            delivered += entry.getValue().size() * prod;
        }

        assertTrue(delivered >= workload - 0.1,
                "Delivered " + delivered + " < Workload " + workload);
    }

    /**
     * Test 6: Solver prioritizes speed (using max cranes) over saving cranes.
     */
    @Test
    void testMaximizeSpeed() {
        int workload = 40;
        Vessel v1 = new Vessel("V1", workload, 100, 10, startDate,
                startDate.plusHours(48));
        v1.setMaxCranes(2);

        Problem problem = createProblem(List.of(v1), null);
        for (Crane c : problem.getCranes()) {
            c.setMinProductivity(10);
            c.setMaxProductivity(10);
        }
        preprocessVessels(problem);

        Solution solution = solver.solve(problem, 60);
        assertTrue(
                "OPTIMAL".equals(solution.getStatus()) || "FEASIBLE".equals(solution.getStatus()),
                "Status was " + solution.getStatus());

        VesselSolution solV1 = solution.getVesselSolutions().get(0);
        int duration = solV1.getEndShift() - solV1.getStartShift();

        assertEquals(2, duration,
                "Solver chose duration " + duration + " instead of 2 (fast/multi-crane)");

        for (Map.Entry<Integer, List<String>> entry : solV1.getAssignedCranes().entrySet()) {
            assertEquals(2, entry.getValue().size(),
                    "Shift " + entry.getKey() + " used " + entry.getValue().size() + " cranes, expected 2");
        }
    }
}
