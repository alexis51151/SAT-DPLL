package FormulaGenerator;

import Solver.CNF;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    @Test
    void generate3SATTest5() throws IOException {
        RandomGenerator generator = new RandomGenerator(100,300);
        File f = new File("generatorTest.cnf");
        FileWriter fw = new FileWriter(f);
        for (int i = 0; i < 10; i++) {
            CNF cnf = generator.generate3SAT();
//            System.out.printf(cnf.toDimacs());
            fw.write(cnf.toDimacs() + "\n");
        }
        fw.close();
    }


}