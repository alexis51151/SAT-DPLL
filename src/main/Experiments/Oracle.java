package Experiments;

import FormulaGenerator.RandomGenerator;
import Solver.CNF;
import Solver.DPLL;
import Solver.Heuristics.*;
import Solver.Prop;
import Solver.TruthAssignment;

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
            DPLL solver1 = new DPLL(props, h1);
            DPLL solver2 = new DPLL(props, h2);
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
            DPLL solver = new DPLL(new ArrayList<>(props));
            // Verify agreement between the solvers on satisfiability for formula phi
            HashMap<Prop, Boolean> tau = solver.solve_iter(phi);
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
            DPLL solver = new DPLL(new ArrayList<>(props), new JeroslowWang());
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
        int Nmax = 50;
        int nb = 100;
        int seed = 2587;
        oracleIter(Nmax, nb, seed);
        oracleRec(Nmax, nb, seed);
    }
}
