package Solver;

import java.util.*;

public class DNF implements Form{
    private final List<List<Literal>> disjunctions;

    public DNF(List<List<Literal>> disjunctions) {
        this.disjunctions = disjunctions;
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        assert false;
        return false;
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        assert false;
        return null;
    }

    public CNF toCNF() {
        // Cartesian product construction
        Set<Set<Literal>> taus = new HashSet<>();
        for (List<Literal> disjunction : disjunctions) {
            if (taus.isEmpty()) {
                for (Literal l : disjunction) {
                    Set<Literal> set = new HashSet<>();
                    set.add(l);
                    taus.add(set);
                }
            } else {
                Set<Set<Literal>> newTaus = new HashSet<>();
                for (Set<Literal> tau : taus) {
                    for (Literal l : disjunction) {
                        Set<Literal> set = new HashSet<>(tau);
                        if (!tau.contains(l.negation())) {
                            set.add(l);
                            newTaus.add(set);
                        }
                    }
                }
                taus = newTaus;
            }
        }
        List<Clause> clauses = new ArrayList<>();
        for(Set<Literal> tau : taus) {
            clauses.add(new Clause(new ArrayList<>(tau)));
        }
        return new CNF(clauses);
    }

}
