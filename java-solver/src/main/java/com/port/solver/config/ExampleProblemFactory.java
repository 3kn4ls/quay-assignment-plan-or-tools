package com.port.solver.config;

import com.port.solver.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory for creating the example problem instance.
 * Direct port of main.py create_example_problem().
 */
public class ExampleProblemFactory {

    /**
     * Create cranes with different types and coverage.
     * Port of create_cranes() from main.py.
     */
    public static List<Crane> createCranes(int berthLength) {
        List<Crane> cranes = new ArrayList<>();

        // 6 STS cranes covering 0-1400m
        for (int i = 1; i <= 6; i++) {
            cranes.add(new Crane(
                    String.format("STS-%02d", i),
                    "STS Crane " + i,
                    CraneType.STS,
                    0, 1400,
                    100, 130));
        }

        // 4 MHC cranes covering 1000-berth_length
        for (int i = 1; i <= 4; i++) {
            cranes.add(new Crane(
                    String.format("MHC-%02d", i),
                    "MHC Crane " + i,
                    CraneType.MHC,
                    1000, berthLength,
                    60, 90));
        }

        return cranes;
    }

    /**
     * Generate consecutive shifts starting from a date (DDMMYYYY format).
     * Port of generate_shifts() from main.py.
     */
    public static List<Shift> generateShifts(String startDateStr, int numTotalShifts) {
        int d = Integer.parseInt(startDateStr.substring(0, 2));
        int m = Integer.parseInt(startDateStr.substring(2, 4));
        int y = Integer.parseInt(startDateStr.substring(4));

        LocalDate currentDate = LocalDate.of(y, m, d);
        List<Shift> shifts = new ArrayList<>();
        int[] shiftStarts = {0, 6, 12, 18};

        while (shifts.size() < numTotalShifts) {
            for (int startHour : shiftStarts) {
                if (shifts.size() >= numTotalShifts) break;

                LocalDateTime startDt = LocalDateTime.of(currentDate, LocalTime.of(startHour, 0));
                LocalDateTime endDt = startDt.plusHours(6);

                shifts.add(new Shift(shifts.size(), startDt, endDt));
            }
            currentDate = currentDate.plusDays(1);
        }

        return shifts;
    }

    /**
     * Create the realistic example problem instance.
     * Direct port of create_example_problem() from main.py.
     */
    public static Problem createExampleProblem() {
        Berth berth = new Berth(2000, null, Map.of(0, 16.0, 1200, 12.0));

        int numShifts = 12;
        List<Shift> shifts = generateShifts("31122025", numShifts);
        LocalDateTime baseTime = shifts.get(0).getStartTime();

        // Helper to get datetime from shift index + hour offset
        // Port of get_dt() from main.py
        java.util.function.BiFunction<Integer, Integer, LocalDateTime> getDt = (shiftIdx, hourOffset) -> {
            if (shiftIdx >= shifts.size()) {
                return shifts.get(shifts.size() - 1).getEndTime()
                        .plusHours(6L * (shiftIdx - shifts.size() + 1));
            }
            return shifts.get(shiftIdx).getStartTime().plusHours(hourOffset);
        };

        // Create vessels
        List<Vessel> vessels = new ArrayList<>();

        // V1-MSC
        vessels.add(new Vessel("V1-MSC", 800, 300, 14.0,
                getDt.apply(0, 0), null,
                4, ProductivityMode.MAX,
                List.of(
                        new YardQuayZonePreference(2, 600),
                        new YardQuayZonePreference(1, 200)
                )));

        // V2-MAERSK
        vessels.add(new Vessel("V2-MAERSK", 600, 250, 13.0,
                getDt.apply(0, 2), null,
                3, ProductivityMode.INTERMEDIATE,
                List.of(
                        new YardQuayZonePreference(1, 500),
                        new YardQuayZonePreference(2, 100)
                )));

        // V3-COSCO
        vessels.add(new Vessel("V3-COSCO", 500, 280, 14.5,
                getDt.apply(0, 0), null,
                3, ProductivityMode.MIN,
                List.of(new YardQuayZonePreference(3, 500))));

        // V4-CMA
        vessels.add(new Vessel("V4-CMA", 400, 200, 12.0,
                getDt.apply(1, 0), null,
                3, ProductivityMode.INTERMEDIATE,
                List.of(new YardQuayZonePreference(4, 400))));

        // V5-HAPAG
        vessels.add(new Vessel("V5-HAPAG", 350, 180, 11.0,
                getDt.apply(1, 0), null,
                2, ProductivityMode.MAX,
                List.of(new YardQuayZonePreference(2, 350))));

        // V6-ONE
        vessels.add(new Vessel("V6-ONE", 700, 290, 13.5,
                getDt.apply(2, 0), null,
                3, ProductivityMode.INTERMEDIATE, null));

        // V7-EVERGREEN
        vessels.add(new Vessel("V7-EVERGREEN", 900, 330, 15.0,
                getDt.apply(2, 0), null,
                4, ProductivityMode.MAX,
                List.of(new YardQuayZonePreference(3, 900))));

        // V8-HMM
        vessels.add(new Vessel("V8-HMM", 450, 220, 12.5,
                getDt.apply(3, 0), null,
                3, ProductivityMode.INTERMEDIATE, null));

        // V9-YANGMING
        vessels.add(new Vessel("V9-YANGMING", 550, 260, 13.8,
                getDt.apply(3, 0), null,
                3, ProductivityMode.MIN, null));

        // V10-ZIM
        vessels.add(new Vessel("V10-ZIM", 400, 210, 11.5,
                getDt.apply(4, 0), null,
                2, ProductivityMode.INTERMEDIATE, null));

        // V11-WANHAI
        vessels.add(new Vessel("V11-WANHAI", 300, 190, 10.5,
                getDt.apply(4, 0), null,
                2, ProductivityMode.INTERMEDIATE, null));

        // V12-PIL
        vessels.add(new Vessel("V12-PIL", 600, 270, 13.2,
                getDt.apply(5, 0), null,
                3, ProductivityMode.INTERMEDIATE, null));

        // Pre-process vessels
        preprocessVessels(vessels, shifts, numShifts);

        // Create cranes
        List<Crane> cranes = createCranes(berth.getLength());

        // Availability logic
        List<String> allCraneIds = cranes.stream().map(Crane::getId).collect(Collectors.toList());
        Map<Integer, List<String>> availability = new HashMap<>();

        for (int t = 0; t < numShifts; t++) {
            List<String> avail = new ArrayList<>(allCraneIds);
            if (t < 2) {
                avail.remove("STS-01");
            }
            availability.put(t, avail);
        }

        // Forbidden zones
        List<ForbiddenZone> forbiddenZones = List.of(
                new ForbiddenZone(400, 600, 2, 4, "Quay Wall Maintenance A"),
                new ForbiddenZone(1500, 1600, 6, 8, "Dredging Operations B")
        );

        // Yard zones
        List<YardQuayZone> yardZones = List.of(
                new YardQuayZone(1, "Zone A", 0, 500),
                new YardQuayZone(2, "Zone B", 500, 1000),
                new YardQuayZone(3, "Zone C", 1000, 1500),
                new YardQuayZone(4, "Zone D", 1500, 2000)
        );

        return new Problem(berth, vessels, cranes, shifts, availability,
                forbiddenZones, yardZones, new HashMap<>());
    }

    /**
     * Pre-process vessels to populate arrival shift indices and fractions.
     * Port of the preprocessing loop from main.py.
     */
    public static void preprocessVessels(List<Vessel> vessels, List<Shift> shifts, int numShifts) {
        for (Vessel v : vessels) {
            v.setArrivalShiftIndex(-1);
            v.setArrivalFraction(1.0);

            for (int t = 0; t < shifts.size(); t++) {
                Shift s = shifts.get(t);
                LocalDateTime arrTime = v.getArrivalTime();
                if (!arrTime.isBefore(s.getStartTime()) && arrTime.isBefore(s.getEndTime())) {
                    v.setArrivalShiftIndex(t);
                    double totalDur = java.time.Duration.between(s.getStartTime(), s.getEndTime()).toSeconds();
                    double availDur = java.time.Duration.between(arrTime, s.getEndTime()).toSeconds();
                    v.setArrivalFraction(totalDur > 0 ? availDur / totalDur : 0);
                    break;
                }
            }

            // If arrival is before first shift
            if (v.getArrivalTime().isBefore(shifts.get(0).getStartTime())) {
                v.setArrivalShiftIndex(0);
                v.setArrivalFraction(1.0);
            }

            // If arrival is after last shift
            if (v.getArrivalShiftIndex() == -1 &&
                    !v.getArrivalTime().isBefore(shifts.get(shifts.size() - 1).getEndTime())) {
                v.setArrivalShiftIndex(numShifts);
            }

            // Departure shift index
            v.setDepartureShiftIndex(numShifts);
            if (v.getDepartureDeadline() != null) {
                for (int t = 0; t < shifts.size(); t++) {
                    Shift s = shifts.get(t);
                    LocalDateTime dep = v.getDepartureDeadline();
                    if (!dep.isBefore(s.getStartTime()) && !dep.isAfter(s.getEndTime())) {
                        v.setDepartureShiftIndex(t);
                        break;
                    }
                }
            }

            // Available shifts
            int startIdx = v.getArrivalShiftIndex();
            if (startIdx < numShifts) {
                v.setAvailableShifts(IntStream.range(startIdx, numShifts).boxed().collect(Collectors.toList()));
            } else {
                v.setAvailableShifts(Collections.emptyList());
            }
        }
    }
}
