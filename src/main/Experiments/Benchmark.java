package Experiments;


import FormulaGenerator.RandomGenerator;
import Solver.*;
import Solver.Heuristics.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
        RandomGenerator generator = new RandomGenerator(n, l, 785);
        List<Prop> props = new ArrayList<>(generator.getProps());
        Heuristic heuristic = new JeroslowWang();
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), heuristic);

        float sat = 0;
        int newNb = nb;
        for (int i = 0; i < nb; i++) {
            System.out.println("i = " + i);
            CNF phi = generator.generate3SAT();
            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<HashMap<Prop, Boolean>> task = () -> solver.solve(phi);
            Future<HashMap<Prop, Boolean>> future = executor.submit(task);
            try {
                HashMap<Prop, Boolean> tau = future.get(timeout, TimeUnit.SECONDS);
                if (tau != null) {
                    sat++;
                }
//                else {
//                    System.out.println("UNSAT");
//                    System.out.println(phi.toDimacs());
//                }
            } catch (TimeoutException | InterruptedException | ExecutionException ex) {
                // handle the timeout
                newNb--;
            }
        }
        return sat / newNb;
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
            System.out.println("Probability of satisfiability of random formulas for L/N = " + r + " for N = 150 is equal to " + p150);
        }
        outr150.close();
    }

    /**
     *
     * @param n         Nb of vars.
     * @param l         Nb of clauses.
     * @param nb        Nb of experiments.
     * @param timeout   Timeout on an experiment.
     * @param heuristic Heuristic studied.
     * @return          Median of DPLL calls and compute time.
     */
    public static Pair<Long, Integer> measure(int n, int l, int nb, int timeout, Heuristic heuristic) {
        // Setting up the solver + formulas generator
        RandomGenerator generator = new RandomGenerator(n, l,1);
        List<Prop> props = new ArrayList<>(generator.getProps());
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), heuristic);
        // Performance measurements
        List<Long> durations = new ArrayList<>();
        List<Integer> nbCalls = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            CNF phi = generator.generate3SAT();
            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<HashMap<Prop, Boolean>> task = () -> solver.solve(phi);
            Future<HashMap<Prop, Boolean>> future = executor.submit(task);
            try {
                long startTime = System.currentTimeMillis();
                HashMap<Prop, Boolean> tau = future.get(timeout, TimeUnit.SECONDS);
                int nbc = solver.getNbCalls();
                long endTime = System.currentTimeMillis();
                long timeMilli= endTime - startTime;
                durations.add(timeMilli);
                nbCalls.add(nbc);
            } catch (TimeoutException | InterruptedException | ExecutionException ex) {
                // handle the timeout
            }
        }
        // Compute the median
        Long medianTime;
        int medianCalls;
        // 1) sort the arrays
        Collections.sort(durations);
        Collections.sort(nbCalls);
        // 2) Retrieve the median
        if (durations.size() % 2 == 0) {
            medianTime = durations.get(durations.size() / 2);
        } else {
            medianTime = (durations.get((durations.size()-1)/2) + durations.get((durations.size()+1)/2))/2;
        }
        if (nbCalls.size() % 2 == 0) {
            medianCalls = nbCalls.get(nbCalls.size() / 2);
        } else {
            medianCalls = (nbCalls.get((nbCalls.size()-1)/2) + nbCalls.get((nbCalls.size()+1)/2))/2;
        }
        return new Pair<>(medianTime, medianCalls);
    }

    public static void printMeasurements(int N, Heuristic heuristic) throws IOException {
        /* Files to write the results */
        File f = new File("measurements_N150" + heuristic.toString() + ".txt");
        FileWriter fw = new FileWriter(f);
        for (float r = 3; r <= 6; r+= 0.2) {
            System.out.println("--------r = " + r + "--------");
            Pair<Long, Integer> pair = measure(N, (int) (N*r), 100, 20, heuristic);
            Long medianTime = pair.a;
            Integer medianCalls = pair.b;
            fw.write(new DecimalFormat("#.##").format(medianTime) + "\t");
            System.out.println("Compute time on random formulas for L/N = " + r + " for N = 150 and heuristic " + heuristic + " is equal to " + medianTime);
            fw.write(medianCalls + "\n");
            System.out.println("Nb of DPLL calls on random formulas for L/N = " + r + " for N = 150 and heuristic " + heuristic + " is equal to " + medianCalls);
        }
        fw.close();

    }


     public static void main(String[] args) throws IOException {
//        printMaxPropsSupported(new RandomChoice());
//        printSATProbability();
//        printMeasurements(150, new JeroslowWang());
        printMeasurements(150, new TwoClauses());
     }

}
