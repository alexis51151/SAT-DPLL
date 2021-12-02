package Solver;

import FormulaGenerator.RandomGenerator;
import Solver.Heuristics.Heuristic;
import Solver.Heuristics.JeroslowWang;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class DPLLIterativeTest {

    @Test
    void solve_iterTest1() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l)));
        Clause clause2 = new Clause(new ArrayList<>(List.of(notp_l)));
//        Clause clause3 = new Clause(new ArrayList<>(List.of(notq_l)));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2)));
//        CNF cnf2 = new CNF(new ArrayList<>(Arrays.asList(clause1, clause3)));

        // DPLL class
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(Arrays.asList(p,q)));


        // Test for satisfiability
        HashMap<Prop, Boolean> tau = solver.solve(cnf);
        System.out.println(cnf);
        for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
            Literal l = new Literal(deduction.getKey(), !deduction.getValue());
            System.out.println(l);
        }
    }

    @Test
    void solve_iterTest2() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal r_l = new Literal(r, false);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l, r_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(p_l.negation(), q_l.negation())));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(p_l.negation(), r_l.negation())));
//        Clause clause4 = new Clause(new ArrayList<>(Arrays.asList(q_l.negation(), r_l.negation())));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(Arrays.asList(p,q,r)));


        // Test for satisfiability
        HashMap<Prop, Boolean> tau = solver.solve(cnf);
        System.out.println(cnf);
        for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
            Literal l = new Literal(deduction.getKey(), !deduction.getValue());
            System.out.println(l);
        }
    }

    @Test
    void solve_iterTest3() {
        // Definition of literals
        Prop n11= new Prop("n11");
        Prop n12 = new Prop("n12");
        Prop n21 = new Prop("n21");
        Prop n22 = new Prop("n22");
        Literal n11_l = new Literal(n11, false);
        Literal n12_l = new Literal(n12, false);
        Literal n21_l = new Literal(n21, false);
        Literal n22_l = new Literal(n22, false);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(n11_l, n12_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(n21_l,n22_l)));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(n11_l.negation(), n12_l.negation())));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(Arrays.asList(n11, n12, n21, n22)));


        // Test for satisfiability
        HashMap<Prop, Boolean> tau = solver.solve(cnf);
        System.out.println(cnf);
        for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
            Literal l = new Literal(deduction.getKey(), !deduction.getValue());
            System.out.println(l);
        }
    }

    @Test
    void solve_iterTest4() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal r_l = new Literal(r, false);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l, r_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(p_l.negation(), q_l.negation())));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(p_l.negation(), r_l.negation())));
        Clause clause4 = new Clause(new ArrayList<>(Arrays.asList(q_l.negation(), r_l.negation())));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3, clause4)));

        // DPLL class
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(Arrays.asList(p,q,r)));


        // Test for satisfiability
        HashMap<Prop, Boolean> tau = solver.solve(cnf);
        System.out.println(cnf);
        for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
            Literal l = new Literal(deduction.getKey(), !deduction.getValue());
            System.out.println(l);
        }
    }

    @Test
    void solve_iterTest5() {
        RandomGenerator generator = new RandomGenerator(3, 6);
        CNF cnf = generator.generate3SAT();
        System.out.println(cnf.toDimacs());
        List<Prop> props = generator.getProps();

        // DPLL class
        DPLLIterative solver = new DPLLIterative(props);


        // Test for satisfiability
        HashMap<Prop, Boolean> tau = solver.solve(cnf);
        System.out.println(cnf);
        for (Map.Entry<Prop, Boolean> deduction : tau.entrySet()) {
            Literal l = new Literal(deduction.getKey(), !deduction.getValue());
            System.out.println(l);
        }
    }

    
    @Test
    void solveEinsteinPuzzle() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(puzzle.getAP()));
        System.out.println(cnf);
        HashMap<Prop, Boolean> res = solver.solve(cnf);
        // {c21,b44,c44,h42,b25,n41,h25,p42,n24,p23,b51,b33,c32,b12,c55,h53,h31,c13,h14,n55,p31,n32,n13,p55,p14}
        TruthAssignment expected = new TruthAssignment(new HashSet<>(Arrays.asList(new Prop("c21"), new Prop("b24"), new Prop("c45"), new Prop("b45"),
                new Prop("h42"), new Prop("h24"), new Prop("n41"), new Prop("p42"), new Prop("n25"), new Prop("p23"),
                new Prop("b51"), new Prop("b33"), new Prop("c32"),new Prop("c54"), new Prop("b12"), new Prop("h53"),
                new Prop("h31"), new Prop("c13"), new Prop("h15"), new Prop("p31"), new Prop("n32"), new Prop("n54"),
                new Prop("n13"), new Prop("p54"), new Prop("p15"))));
        System.out.println("Nb of entries = " + res.size());
        HashSet<Prop> props = new HashSet<>();
        for (Map.Entry<Prop, Boolean> deduction : res.entrySet()) {
            if (deduction.getValue()) {
                props.add(deduction.getKey());
            }
        }
        TruthAssignment tau = new TruthAssignment(props);
        EinsteinPuzzle.printTruthAssignement(tau);
        assertEquals(25, tau.getTau().size());

        assertEquals(expected, tau);
    }

    @Test
    public void debugSATProbability() {
        int n = 50;
        int l = 150;
        int nb = 100;
        int timeout = 20;
        RandomGenerator generator = new RandomGenerator(n, l );
        List<Prop> props = new ArrayList<>(generator.getProps());
        Heuristic heuristic = new JeroslowWang();
        DPLLIterative solver = new DPLLIterative(new ArrayList<>(props), heuristic);

        CNF phi = generator.generate3SAT();
        HashMap<Prop, Boolean> tau = solver.solve(phi);
        if (tau == null) {
            System.out.println("Returned false");
        }




//        float sat = 0;
//        for (int i = 0; i < nb; i++) {
//            CNF phi = generator.generate3SAT();
//            if (i == 4) {
//                HashMap<Prop, Boolean> tau = solver.solve(phi);
//                if (tau != null) {
//                    sat++;
//                } else {
//                    System.out.println("Returned false");
//                }
//            }
//        }
    }



}