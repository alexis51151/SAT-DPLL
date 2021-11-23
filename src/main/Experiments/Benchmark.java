package Experiments;


import FormulaGenerator.RandomGenerator;
import Solver.CNF;
import Solver.DPLL;
import Solver.Heuristics.Heuristic;
import Solver.Heuristics.JeroslowWang;
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
                    return solver.SAT(phi);
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

    public static float SATProbability(int n, int l, int nb) {
        RandomGenerator generator = new RandomGenerator(n, l);
        List<Prop> props = generator.getProps();
        Heuristic heuristic = new JeroslowWang();
        DPLL solver = new DPLL(props, heuristic);

        float sat = 0;
        for (int i = 0; i < nb; i++) {
            CNF phi = generator.generate3SAT();
            TruthAssignment tau = solver.SAT(phi);
            if (tau != null) {
                sat++;
            }
        }
        return sat / nb;
    }

    public static void printSATProbability() {
        float p100, p150;

        // N = 100
        for (float r = 1; r <= 3; r+= 0.2) {
            int N = 50;
            p100 = SATProbability(N, (int) (N*r), 100);
            System.out.println("Probability of satisfiability of random formulas for L/N = " + r + " for N = 100 is equal to " + p100);
        }

//        // N = 150
//        for (int r = 3; r < 6; r+= 0.2) {
//            int N = 150;
//            p150 = SATProbability(N, r*N, 10);
//            System.out.println("Probability of satisfiability of random formulas for L/N = " + r + " for N = 150 is equal to " + p150);
//        }
    }





    public static void main(String[] args) {
//        printMaxPropsSupported(new TwoClauses());
        printSATProbability();
    }

}
