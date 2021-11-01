package Solver;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LiteralTest {

    @Test
    public void literalTest1() {
        Prop p = new Prop("p");
        TruthAssignment tau = new TruthAssignment(new HashSet<>(List.of(p)));
        Literal l = new Literal(p, false);
        assertTrue(l.eval(tau));
    }

    @Test
    public void literalTest2() {
        Prop p = new Prop("p");
        TruthAssignment tau = new TruthAssignment(new HashSet<>(List.of(p)));
        Literal l = new Literal(p, true);
        assertFalse(l.eval(tau));
    }

    @Test
    public void literalTest3() {
        Prop p = new Prop("p");
        TruthAssignment tau = new TruthAssignment(new HashSet<>());
        Literal l = new Literal(p, true);
        assertTrue(l.eval(tau));
    }

    @Test
    public void substituteTest1() {
        Prop p = new Prop("p");
        Literal l = new Literal(p, false);
        Form f = l.substitute("p", true);
        assertEquals(f, new ConstForm(true));
    }

    @Test
    public void substituteTest2() {
        Prop p = new Prop("p");
        Literal l = new Literal(p, false);
        Form f = l.substitute("q", true);
        assertEquals(f, l);
    }

    @Test
    public void substituteTest3() {
        Prop p = new Prop("p");
        Literal l = new Literal(p, true);
        Form f = l.substitute("p", true);
        assertEquals(f, new ConstForm(false));
    }



}