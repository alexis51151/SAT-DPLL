package Solver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    }
}