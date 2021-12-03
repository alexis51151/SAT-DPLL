import Parser.Parser;
import Solver.Heuristics.Heuristic;
import Solver.CNF;
import Solver.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Solver {


    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: solver <DIMACS-format file> <heuristic: JeroslowWang/RandomChoice/TwoClauses>");
            System.exit(1);
            return;
        }
        String path = args[0];
        Heuristic heuristic = Heuristic.getHeuristic(args[1]);
        CNF cnf;
        List<Prop> props;
        try {
            Pair<CNF, List<Prop>> pair = Parser.parse("test.cnf");
            cnf = pair.a;
            System.out.println("Formula = " + cnf.toString());
            props = pair.b;
        } catch (IOException e) {
            System.out.println("Error: could not find or read the input file!");
            System.exit(1);
            return;
        }
        // Initialize the solver
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), heuristic);
        // Timeout of 60 seconds.
        int timeout = 60;
        HashMap<Prop, Boolean> tau = null;
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<HashMap<Prop, Boolean>> task = () -> solver.solve(cnf);
        Future<HashMap<Prop, Boolean>> future = executor.submit(task);
        try {
            tau = future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            // handle the timeout
            System.out.println("Error: timeout!");
            System.exit(1);
            return;
        }
        // Print the truth assignment or unsat
        if (tau == null) {
            System.out.println("UNSAT");
        }
        else {
            System.out.print("Truth assignment = ");
            for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
                Literal l = new Literal(deduction.getKey(), !deduction.getValue());
                System.out.print(l + " ");
            }
            System.out.println();
        }
        System.exit(0);
    }
}
