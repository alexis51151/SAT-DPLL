package Solver;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DNFTest {
    @Test
    void toCNFTest1() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal r_l = new Literal(r, false);
        // Definition of disjunctions
        List<Literal> disjunction1 = new ArrayList<>(Arrays.asList(p_l, q_l.negation()));
        List<Literal> disjunction2 = new ArrayList<>(Arrays.asList(q_l, r_l));
        List<List<Literal>> disjunctions = new ArrayList<>(Arrays.asList(disjunction1, disjunction2));
        DNF dnf = new DNF(disjunctions);
        CNF cnf = dnf.toCNF();
        System.out.println(cnf);
    }

}