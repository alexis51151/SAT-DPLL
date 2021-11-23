package FormulaGenerator;

import Solver.CNF;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @Test
    void generate3SATTest1() {
        RandomGenerator generator = new RandomGenerator(3,1);
        CNF cnf = generator.generate3SAT();
        System.out.println(cnf);
    }

    @Test
    void generate3SATTest2() {
        RandomGenerator generator = new RandomGenerator(50,5);
        CNF cnf = generator.generate3SAT();
        System.out.println(cnf);
    }

    @Test
    void generate3SATTest3() {
        RandomGenerator generator = new RandomGenerator(150,20);
        CNF cnf = generator.generate3SAT();
        System.out.println(cnf);
    }


    @Test
    void generate3SATTest4() {
        RandomGenerator generator = new RandomGenerator(150,200);
        CNF cnf = generator.generate3SAT();
        assertEquals(200, cnf.getClauses().size());
    }
}