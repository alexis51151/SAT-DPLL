package Solver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EinsteinPuzzleTest {

    @Test
    void getCnf() {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        System.out.println(cnf);
    }
}