package Solver.Heuristics;

import FormulaGenerator.RandomGenerator;
import Solver.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DLISTest {

    @Test
    void SATTest4() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        Prop s = new Prop("s");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notq_l = new Literal(q, true);
        Literal r_l = new Literal(r, false);
        Literal s_l = new Literal(s, false);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, notq_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(q_l, r_l)));
        Clause clause3 = new Clause(new ArrayList<>(List.of(s_l)));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(p,q,r,s), new DLIS());

        // Test for satisfiability
        TruthAssignment tau = solver.SAT(cnf);
        System.out.println(tau);
        assertTrue(tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(p,q,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(p,q,r,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(r,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(r,s,p)))));
    }

    @Test
    void SATTest9() {
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
        DPLL solver = new DPLL(Arrays.asList(n11, n12, n21, n22), new DLIS());

        // Test for satisfiability
        TruthAssignment tau = solver.SAT(cnf);
        System.out.println(cnf);
        System.out.println(tau);
    }

    @Test
    void SATTest8() {
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
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(n11_l, n12_l, n21_l, n22_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(n11_l.negation(), n12_l.negation())));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(n21_l.negation(), n22_l.negation())));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(n11, n12, n21, n22), new DLIS());

        // Test for satisfiability
        TruthAssignment tau = solver.SAT(cnf);
        System.out.println(cnf);
        System.out.println(tau);
    }




    @Test
    void solveEinsteinPuzzle() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        DPLL solver = new DPLL(puzzle.getAP(), new DLIS());
        TruthAssignment tau = solver.SAT(cnf);
//        System.out.println(cnf);
//        System.out.println(tau);
        // {c21,b44,c44,h42,b25,n41,h25,p42,n24,p23,b51,b33,c32,b12,c55,h53,h31,c13,h14,n55,p31,n32,n13,p55,p14}
        TruthAssignment expected = new TruthAssignment(new HashSet<>(Arrays.asList(new Prop("c21"), new Prop("b24"), new Prop("c45"), new Prop("b45"),
                new Prop("h42"), new Prop("h24"), new Prop("n41"), new Prop("p42"), new Prop("n25"), new Prop("p23"),
                new Prop("b51"), new Prop("b33"), new Prop("c32"),new Prop("c54"), new Prop("b12"), new Prop("h53"),
                new Prop("h31"), new Prop("c13"), new Prop("h15"), new Prop("p31"), new Prop("n32"), new Prop("n54"),
                new Prop("n13"), new Prop("p54"), new Prop("p15"))));
        assertEquals(25, tau.getTau().size());
        EinsteinPuzzle.printTruthAssignement(tau);
        assertEquals(expected, tau);
    }

    @Test
    void randomGeneratorTest() {
        RandomGenerator g = new RandomGenerator(40,60);
        CNF cnf = g.generate3SAT();
        DPLL solver = new DPLL(g.getProps(), new DLIS());
        solver.SAT(cnf);
    }

}