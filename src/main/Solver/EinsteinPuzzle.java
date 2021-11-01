package Solver;

import java.util.*;

public class EinsteinPuzzle {
    private CNF cnf;

    public EinsteinPuzzle() {
        List<Clause> clauses = new ArrayList<>();
        // Names for APs
        Set<String> sets = new HashSet<>(Arrays.asList("n", "h", "b", "c", "p"));
        // Basic problem
        for (String prefix : sets) {
            // Each house has at least one _
            List<Literal> set = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 5; j++) {
                    String symbol = prefix + i + j;
                    set.add(new Literal(symbol, false));
                }
            }
            clauses.add(new Clause(set));
//             Each house has at most one _
            for (int i = 1; i <= 5; i++) {
                for (int k = 1; k <= 5; k++) {
                    for (int j = 1; j < k; j++) {
                        List<Literal> newSet = new ArrayList<>();
                        newSet.add(new Literal(prefix + Integer.toString(i) + Integer.toString(j
                        ), true));
                        newSet.add(new Literal(prefix + Integer.toString(i) + Integer.toString(k
                        ), true));
                        clauses.add(new Clause(newSet));
                    }
                }
            }
        }
        // Hints
        CNF cnf = new CNF(clauses);
        // 1) "The Brit lives in the red house."
        DNF dnf1 = getHouse("n1", "h5");
        cnf.addAll(dnf1.toCNF());


        // Result CNF
        this.cnf = cnf;
    }

    public DNF getHouse(String symbol1, String symbol2) {
        List<List<Literal>> disjunctions = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            List<Literal> conjunction = new ArrayList<>();
            conjunction.add(new Literal(symbol1 + j, false));
            conjunction.add(new Literal(symbol2 + j, false));
            disjunctions.add(conjunction);
        }
        return new DNF(disjunctions);
    }

    public CNF getCnf() {
        return cnf;
    }
}
