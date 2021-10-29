package Solver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CNFTest {

    @Test
    void evalTest() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notq_l = new Literal(q, true);
        Literal r_l = new Literal(r, false);
        // Definition of clauses
        Clause clause1 = new Clause(new ArrayList<>(Arrays.asList(p_l, notq_l)));
        Clause clause2 = new Clause(new ArrayList<>(Arrays.asList(q_l, r_l)));
        // Definition of CNF formula
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2)));
        // Definition of truth assignements
        TruthAssignment tau1 = new TruthAssignment(new HashSet<>(List.of(q)));
        TruthAssignment tau2 = new TruthAssignment(new HashSet<>(List.of(p)));
        TruthAssignment tau3 = new TruthAssignment(new HashSet<>(Arrays.asList(p,q)));
        TruthAssignment tau4 = new TruthAssignment(new HashSet<>(Arrays.asList(p,r)));
        TruthAssignment tau5 = new TruthAssignment(new HashSet<>(List.of(r)));
        TruthAssignment tau6 = new TruthAssignment(new HashSet<>(Arrays.asList(q,r)));
        assertFalse(cnf.eval(tau1));
        assertFalse(cnf.eval(tau2));
        assertTrue(cnf.eval(tau3));
        assertTrue(cnf.eval(tau4));
        assertTrue(cnf.eval(tau5));
        assertFalse(cnf.eval(tau6));
    }

    @Test
    void substituteTest1() {
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
        // Definition of CNF formula
        CNF cnf = new CNF(new ArrayList<>(Arrays.asList(clause1, clause2, clause3)));
        // Definition of substitutions
        Form sub1 = cnf.substitute("q", false);
        Form sub2 = cnf.substitute("q", true);
        Form sub3 = cnf.substitute("s", true);
        assertEquals(sub1, new CNF(new ArrayList<>(Arrays.asList(new Clause(new ArrayList<>(List.of(r_l))), new Clause(new ArrayList<>(List.of(s_l)))))));
        assertEquals(sub2, new CNF(new ArrayList<>(Arrays.asList(new Clause(new ArrayList<>(List.of(p_l))), new Clause(new ArrayList<>(List.of(s_l)))))));
        assertEquals(sub3, new CNF(new ArrayList<>(Arrays.asList(clause1, clause2))));
    }
}