package Experiments;

import FormulaGenerator.RandomGenerator;
import Solver.*;
import Solver.RecursiveHeuristics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Oracle {

    public static void oracle(Heuristic h1, Heuristic h2, int Nmax, int nb) {
        Random rand = new Random();
        int disagreements = 0;
        for (int i = 0; i < nb; i++) {
            // Generate a random formula
            int N = rand.nextInt(3, Nmax);
            int L = rand.nextInt(3,6) * N;
            RandomGenerator generator = new RandomGenerator(N, L);
            CNF phi = generator.generate3SAT();
            List<Prop> props = generator.getProps();
            // Generate two instances of the DPLL solver with the given heuristics
            DPLLRecursive solver1 = new DPLLRecursive(props, h1);
            DPLLRecursive solver2 = new DPLLRecursive(props, h2);
            // Verify agreement between the solvers on satisfiability for formula phi
            if ((solver1.SAT(phi) == null && solver2.SAT(phi) != null) || (solver1.SAT(phi) != null && solver2.SAT(phi) == null))
                disagreements++;
        }
        System.out.println("There are " + disagreements + " disagreements between the " + h1.toString() + " and the " + h2.toString() + ".");
    }

    public static void oracleIter(int Nmax, int nb, int seed) {
        Random rand = new Random(seed);
        int disagreements = 0;
        for (int i = 0; i < nb; i++) {
            // Generate a random formula
            int N = rand.nextInt(3, Nmax);
            int L = rand.nextInt(3,6) * N;
            RandomGenerator generator = new RandomGenerator(N, L,seed);
            CNF phi = generator.generate3SAT();
            List<Prop> props = generator.getProps();
            // Generate two instances of the DPLL solver with the given heuristics
            DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), new Solver.Heuristics.JeroslowWang());
            // Verify agreement between the solvers on satisfiability for formula phi
            HashMap<Prop, Boolean> tau = solver.solve(phi);
            if (tau == null)
                disagreements++;
        }
        System.out.println("There are " + disagreements + " nulls ");
    }

    public static void oracleRec(int Nmax, int nb, int seed) {
        Random rand = new Random(seed);
        int disagreements = 0;
        for (int i = 0; i < nb; i++) {
            // Generate a random formula
            int N = rand.nextInt(3, Nmax);
            int L = rand.nextInt(3,6) * N;
            RandomGenerator generator = new RandomGenerator(N, L,seed);
            CNF phi = generator.generate3SAT();
            List<Prop> props = generator.getProps();
            // Generate two instances of the DPLL solver with the given heuristics
            DPLLRecursive solver = new DPLLRecursive(new ArrayList<>(props), new JeroslowWang());
            // Verify agreement between the solvers on satisfiability for formula phi
            TruthAssignment tau = solver.SAT(phi);
            if (tau == null)
                disagreements++;
        }
        System.out.println("There are " + disagreements + " nulls ");
    }





    public static void main(String[] args) throws IOException {
//        oracle(new RandomChoice(), new FirstChoice(), 50, 100);
//        oracle(new RandomChoice(), new TwoClauses(), 50, 100);
//        oracle(new FirstChoice(), new TwoClauses(), 50, 100);
//        oracle(new RandomChoice(), new JeroslowWang(), 50, 100);
//        oracle2(new JeroslowWang(), 4, 100);
        int Nmax = 100;
        int nb = 100;
        int seed = 45;
        oracleIter(Nmax, nb, seed);
//        oracleRec(Nmax, nb, seed);
    }
}
