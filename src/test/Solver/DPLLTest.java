package Solver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DPLLTest {

    @Test
    void SATTest1() {
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
        DPLL solver = new DPLL(Arrays.asList(p,q,r,s));

        // Test for satisfiability
        assertTrue(solver.SAT(cnf));
    }

    @Test
    void SATTest2() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(notq_l)));
        Clause clause3 = new Clause(new ArrayList<>(List.of(notp_l)));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(p,q));

        // Test for satisfiability
        assertFalse(solver.SAT(cnf));
    }

    @Test
    void SATTest3() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(notp_l)));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(notq_l)));

        // Definition of CNF formulas
        CNF cnf1 = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2)));
        CNF cnf2 = new CNF(new ArrayList<>(Arrays.asList(clause1, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(p,q));

        // Test for satisfiability
        assertTrue(solver.SAT(cnf1));
        assertTrue(solver.SAT(cnf2));
    }

    @Test
    void SATTruthAssignement1() {
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
        DPLL solver = new DPLL(Arrays.asList(p,q,r,s));

        // Test for satisfiability
        TruthAssignment tau = solver.SATTruthAssignement(cnf);
        System.out.println(tau);
        assertTrue(tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(p,q,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(p,q,r,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(r,s))))
                || tau.equals(new TruthAssignment(new HashSet<>(Arrays.asList(r,s,p)))));
    }

    @Test
    void SATTruthAssignement2() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(notq_l)));
        Clause clause3 = new Clause(new ArrayList<>(List.of(notp_l)));

        // Definition of CNF formulas
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(p,q));

        // Test for satisfiability
        TruthAssignment tau = solver.SATTruthAssignement(cnf);
        assertNull(tau);
    }

    @Test
    void SATTruthAssignement3() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);

        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, q_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(notp_l)));
        Clause clause3 = new Clause(new ArrayList<>(Arrays.asList(notq_l)));

        // Definition of CNF formulas
        CNF cnf1 = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2)));
        CNF cnf2 = new CNF(new ArrayList<>(Arrays.asList(clause1, clause3)));

        // DPLL class
        DPLL solver = new DPLL(Arrays.asList(p,q));

        // Test for satisfiability
        TruthAssignment tau1 = solver.SATTruthAssignement(cnf1);
        TruthAssignment tau2 = solver.SATTruthAssignement(cnf2);

        assertEquals(new TruthAssignment(new HashSet<>(List.of(q))), tau1);
        assertEquals(tau2, new TruthAssignment(new HashSet<>(List.of(p))));
    }

}