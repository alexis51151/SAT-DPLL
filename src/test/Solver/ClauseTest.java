package Solver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClauseTest {

    @Test
    void evalTest1() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        TruthAssignment tau = new TruthAssignment(new HashSet<>(Arrays.asList(q)));
        assertFalse(p.eval(tau));
        assertTrue(q.eval(tau));
        assertFalse(clause.eval(tau));
    }

    @Test
    void evalTest2() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        TruthAssignment tau = new TruthAssignment(new HashSet<>(Arrays.asList(p)));
        assertTrue(p.eval(tau));
        assertFalse(q.eval(tau));
        assertTrue(p_l.eval(tau));
        assertTrue(notq_l.eval(tau));
        assertTrue(clause.eval(tau));
    }

    @Test
    void evalTest3() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        TruthAssignment tau = new TruthAssignment(new HashSet<>(Arrays.asList(p,q)));
        assertTrue(p.eval(tau));
        assertTrue(q.eval(tau));
        assertTrue(p_l.eval(tau));
        assertFalse(notq_l.eval(tau));
        assertTrue(clause.eval(tau));
    }


    @Test
    void substituteTest1() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        Form f = clause.substitute("p", true);
        assertEquals(f, new ConstForm(true));
    }

    @Test
    void substituteTest2() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        Form f = clause.substitute("q", true);
        assertEquals(f, new Clause(new ArrayList<>(List.of(p_l))));
    }

    @Test
    void substituteTest3() {
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Literal p_l = new Literal(p, false);
        Literal notq_l = new Literal(q, true);
        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));

        Form f = clause.substitute("q", false);
        assertEquals(f, new ConstForm(true));
    }

//    @Test
//    void pointersTest4() {
//        Prop p = new Prop("p");
//        Prop q = new Prop("q");
//        Literal p_l = new Literal(p, false);
//        Literal notq_l = new Literal(q, true);
//        Clause clause = new Clause(new ArrayList<>(Arrays.asList(p_l,notq_l)));
//        assertEquals(new ArrayList<>(List.of(clause)), p.getPosClauses());
//        assertEquals(new ArrayList<>(), p.getNegClauses());
//        assertEquals(new ArrayList<>(List.of(clause)), q.getNegClauses());
//        assertEquals(new ArrayList<>(), q.getPosClauses());
//    }


}