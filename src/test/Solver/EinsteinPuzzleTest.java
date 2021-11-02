package Solver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EinsteinPuzzleTest {

    @Test
    void getCnf() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        System.out.println(cnf);
    }

    @Test
    void solveEinsteinPuzzle() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        DPLL solver = new DPLL(puzzle.getAP());
        TruthAssignment tau = solver.SATTruthAssignement(cnf);
        System.out.println(cnf);
        System.out.println(tau);
        // {c21,b44,c44,h42,b25,n41,h25,p42,n24,p23,b51,b33,c32,b12,c55,h53,h31,c13,h14,n55,p31,n32,n13,p55,p14}
        TruthAssignment expected = new TruthAssignment(new HashSet<>(Arrays.asList(new Prop("c21"), new Prop("b44"), new Prop("c44"), new Prop("h42"),
                new Prop("b25"), new Prop("n41"), new Prop("h25"), new Prop("p42"), new Prop("n24"), new Prop("p23"),
                new Prop("b51"), new Prop("b33"), new Prop("c32"), new Prop("b12"), new Prop("c55"), new Prop("h53"),
                new Prop("h31"), new Prop("c13"), new Prop("h14"), new Prop("n55"), new Prop("p31"), new Prop("n32"),
                new Prop("n13"), new Prop("p55"), new Prop("p14"))));
        assertEquals(25, tau.getTau().size());
        assertEquals(expected, tau);
    }
}