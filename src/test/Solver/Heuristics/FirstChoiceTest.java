package Solver.Heuristics;

import Solver.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FirstChoiceTest {

    @Test
    void solveEinsteinPuzzle() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        DPLL solver = new DPLL(puzzle.getAP(), new FirstChoice());
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

}