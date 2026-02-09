package com.port.solver;

import com.port.solver.config.ExampleProblemFactory;
import com.port.solver.engine.CpSatSolver;
import com.port.solver.engine.SolutionPrinter;
import com.port.solver.model.Problem;
import com.port.solver.model.Solution;

/**
 * CLI entry point for the BAP+QCAP solver.
 * Port of main.py main().
 */
public class MainCli {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("  BAP + QCAP Port Terminal Optimization Solver");
        System.out.println("  Using Google OR-Tools CP-SAT (Java)");
        System.out.println("=".repeat(70));

        System.out.println("\n>>> Example: Forbidden Zones (Maintenance) & Specific Cranes");
        Problem problem = ExampleProblemFactory.createExampleProblem();

        System.out.printf("Loaded %d cranes.%n", problem.getCranes().size());
        for (var c : problem.getCranes()) {
            System.out.printf("  - %s (%s): range %d-%dm, prod=%d%n",
                    c.getName(), c.getCraneType().name(),
                    c.getBerthRangeStart(), c.getBerthRangeEnd(),
                    c.getMaxProductivity());
        }

        int timeLimit = 60;
        if (args.length > 0) {
            try {
                timeLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignore) {}
        }

        CpSatSolver solver = new CpSatSolver();
        Solution solution = solver.solve(problem, timeLimit);

        System.out.println(SolutionPrinter.printSolution(problem, solution));
    }
}
