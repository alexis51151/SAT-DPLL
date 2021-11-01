package Solver;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DNFTest {

    @Test
    void constructTaus() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        List<Prop> AP = new ArrayList<>(Arrays.asList(p,q,r));
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal r_l = new Literal(r, false);
        Literal notp_l = new Literal(p, true);
        Literal notq_l = new Literal(q, true);
        Literal notr_l = new Literal(r, true);

        // Definition of disjunctions
        List<Literal> disjunction1 = new ArrayList<>(Arrays.asList(p_l, notq_l));
        List<Literal> disjunction2 = new ArrayList<>(Arrays.asList(q_l, r_l));
        List<List<Literal>> disjunctions = new ArrayList<>(Arrays.asList(disjunction1, disjunction2));
        DNF dnf = new DNF(disjunctions);
        Set<Set<Literal>> taus = dnf.constructTaus(AP);
        Set<Literal> tau1 = new HashSet<>(Arrays.asList(p_l, q_l, r_l));
        Set<Literal> tau2 = new HashSet<>(Arrays.asList(p_l, q_l, notr_l));
        Set<Literal> tau3 = new HashSet<>(Arrays.asList(p_l, notq_l, r_l));
        Set<Literal> tau4 = new HashSet<>(Arrays.asList(notp_l, notq_l, r_l));
        Set<Set<Literal>> expected = new HashSet<>(Arrays.asList(tau1, tau2, tau3, tau4));
        assertEquals(expected, taus);
    }

    @Test
    void toCNFTest1() {
        // Definition of literals
        Prop p = new Prop("p");
        Prop q = new Prop("q");
        Prop r = new Prop("r");
        List<Prop> AP = new ArrayList<>(Arrays.asList(p,q,r));
        Literal p_l = new Literal(p, false);
        Literal q_l = new Literal(q, false);
        Literal notq_l = new Literal(q, true);
        Literal r_l = new Literal(r, false);
        // Definition of disjunctions
        List<Literal> disjunction1 = new ArrayList<>(Arrays.asList(p_l, notq_l));
        List<Literal> disjunction2 = new ArrayList<>(Arrays.asList(q_l, r_l));
        List<List<Literal>> disjunctions = new ArrayList<>(Arrays.asList(disjunction1, disjunction2));
        DNF dnf = new DNF(disjunctions);
        CNF cnf = dnf.toCNF(AP);
        System.out.println(cnf);
    }

}