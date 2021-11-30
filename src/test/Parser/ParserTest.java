package Parser;

import Solver.CNF;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parseTest() throws IOException {
        CNF cnf = Parser.parse("test.cnf");
        System.out.println(cnf);
    }
}