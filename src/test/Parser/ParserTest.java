package Parser;

import Solver.CNF;
import Solver.Pair;
import Solver.Prop;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parseTest() throws IOException {
        Pair<CNF, List<Prop>> pair = Parser.parse("test.cnf");
        System.out.println(pair.a);
    }
}