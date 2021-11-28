package Experiments;


import FormulaGenerator.RandomGenerator;
import Solver.CNF;
import Solver.DPLLIterative;
import Solver.DPLLRecursive;
import Solver.Heuristics.*;
import Solver.Prop;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Benchmark {

    public static int maxPropsSupported(int incr, int timeout, Heuristic heuristic) {
        int N = 3;
        while (N < 20000) {
            int L = 6*N;
            // Generate a random formula
            RandomGenerator generator = new RandomGenerator(N,L);
            CNF phi = generator.generate3SAT();
            List<Prop> props = new ArrayList<>(generator.getProps());
            // Generate an instance of the DPLL solver with the given heuristic
            DPLLIterative solver = new DPLLIterative(props, heuristic);
            System.out.println("------------(N = " + N + ")------------");
            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<HashMap<Prop, Boolean>> task = new Callable<HashMap<Prop, Boolean>>() {
                public HashMap<Prop, Boolean> call() {
                    return solver.solve(phi);
                }
            };
            Future<HashMap<Prop, Boolean>> future = executor.submit(task);
            try {
                HashMap<Prop, Boolean> result = future.get(timeout, TimeUnit.SECONDS);
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


    public static float SATProbability(int n, int l, int nb, int timeout) {
        RandomGenerator generator = new RandomGenerator(n, l);
        List<Prop> props = new ArrayList<>(generator.getProps());
        Heuristic heuristic = new JeroslowWang();
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), heuristic);

        float sat = 0;
        for (int i = 0; i < nb; i++) {
//            if (i % 10 == 0) {
//            System.out.println("i = " + i);
//            }
            CNF phi = generator.generate3SAT();
            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<HashMap<Prop, Boolean>> task = () -> solver.solve(phi);
            Future<HashMap<Prop, Boolean>> future = executor.submit(task);
            try {
                HashMap<Prop, Boolean> tau = future.get(timeout, TimeUnit.SECONDS);
                if (tau != null) {
                    sat++;
                }
            } catch (TimeoutException | InterruptedException | ExecutionException ex) {
                // handle the timeout
            }
        }
        return sat / nb;
    }


    public static void printSATProbability() throws IOException {
        /* Files to write the probabilities */
        File out100 = new File("probs_N100.txt");
        FileWriter outr100 = new FileWriter(out100);
        float p100;

        // N = 100
        for (float r = 3; r <= 6; r+= 0.2) {
            int N = 100;
            System.out.println("--------r = " + r + "--------");
            p100 = SATProbability(N, (int) (N*r), 100,20);
            outr100.write(new DecimalFormat("#.##").format(p100) + "\n");
            System.out.println("Probability of satisfiability of random formulas for L/N = " + r + " for N = 100 is equal to " + p100);
        }
        outr100.close();

        float p150;
        File out150 = new File("probs_N150.txt");
        FileWriter outr150 = new FileWriter(out150);
        // N = 150
        for (float r = 3; r <= 6; r+= 0.2) {
            int N = 150;
            System.out.println("--------r = " + r + "--------");
            p150 = SATProbability(N, (int) (N*r), 100,20);
            outr150.write(new DecimalFormat("#.##").format(p150) + "\n");
            System.out.println("Probability of satisfiability of random formulas for L/N = " + r + " for N = 100 is equal to " + p150);
        }
        outr150.close();
    }





    public static void main(String[] args) throws IOException {
//        printMaxPropsSupported(new RandomChoice());
//        printSATProbability();

    }

}
