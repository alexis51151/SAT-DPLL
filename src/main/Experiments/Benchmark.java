package Experiments;


import FormulaGenerator.RandomGenerator;
import Solver.CNF;
import Solver.DPLL;
import Solver.Heuristics.Heuristic;
import Solver.Heuristics.RandomChoice;
import Solver.Heuristics.TwoClauses;
import Solver.Prop;
import Solver.TruthAssignment;

import java.util.List;
import java.util.concurrent.*;
import java.lang.Math.*;

public class Benchmark {


    public static int maxPropsSupported(int incr, int timeout, Heuristic heuristic) {
        int N = 3;
        while (N < 20000) {
            int L = 6*N;
            // Generate a random formula
            RandomGenerator generator = new RandomGenerator(N,L);
            CNF phi = generator.generate3SAT();
            List<Prop> props = generator.getProps();
            // Generate an instance of the DPLL solver with the given heuristic
            DPLL solver = new DPLL(props, heuristic);
            System.out.println("------------(N = " + N + ")------------");
            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<TruthAssignment> task = new Callable<TruthAssignment>() {
                public TruthAssignment call() {
                    return solver.SATTruthAssignement(phi);
                }
            };
            Future<TruthAssignment> future = executor.submit(task);
            try {
                TruthAssignment result = future.get(timeout, TimeUnit.SECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException ex) {
                // handle the timeout
                break;
            }
            N += incr;
        }
        return java.lang.Math.max(0, N-incr);
    }

    public static void printMaxPropsSupported(Heuristic heuristic) {
        //  Find the maximum value of N
        //  Timeout of 10 seconds
        int N = maxPropsSupported(25,10, heuristic);
        System.out.println("Maximum nb of vars supported by random heuristic: N = " + N);
    }


    public static void main(String[] args) {
        printMaxPropsSupported(new TwoClauses());
    }

}
