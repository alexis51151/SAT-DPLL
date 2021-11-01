package Solver;

import java.util.*;

public class DNF implements Form{
    private List<List<Literal>> disjunctions;

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

    public CNF toCNF(List<Prop> AP) {
        Set<Set<Literal>> taus = constructTaus(AP);
        // Construction of the CNF form
        List<Clause> clauses = new ArrayList<>();
        for (Set<Literal> tau : taus) {
            List<Literal> literals = new ArrayList<>();
            for (Prop p : AP) {
                Literal p_l = new Literal(p, false);
                assert (tau.contains(p_l) || tau.contains(p_l.negation()));
                if (tau.contains(p_l)) {
                    literals.add(p_l);
                } else {
                    literals.add(p_l.negation());
                }
            }
            clauses.add(new Clause(literals));
        }
        return new CNF(clauses);
    }

    public Set<Set<Literal>> constructTaus(List<Prop> AP) {
        // Cartesian product construction
        Set<Set<Literal>> taus = new HashSet<>();
        for (List<Literal> disjunction : disjunctions){
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

        // Truth assignments construction
        Set<Set<Literal>> newTaus = new HashSet<>();
        for (Set<Literal> tau : taus) {
            Set<Set<Literal>> auxTaus = new HashSet<>();
            for (Prop p : AP) {
                if (auxTaus.isEmpty()) {
                    Set<Literal> tau1 = new HashSet<>(tau);
                    Set<Literal> tau2 = new HashSet<>(tau);
                    Literal p_l = new Literal(p, false);
                    Literal notp_l = p_l.negation();
                    if (!tau.contains(p_l) && !tau.contains(notp_l)) {
                        tau1.add(p_l);
                        tau2.add(notp_l);
                    }
                    auxTaus.add(tau1);
                    auxTaus.add(tau2);
                } else {
                    Set<Set<Literal>> newAuxTaus = new HashSet<>();
                    for (Set<Literal> auxTau : auxTaus) {
                        Set<Literal> tau1 = new HashSet<>(auxTau);
                        Set<Literal> tau2 = new HashSet<>(auxTau);
                        Literal p_l = new Literal(p, false);
                        Literal notp_l = p_l.negation();
                        if (!auxTau.contains(p_l) && !auxTau.contains(notp_l)) {
                            tau1.add(p_l);
                            tau2.add(notp_l);
                        }
                        newAuxTaus.add(tau1);
                        newAuxTaus.add(tau2);
                    }
                    auxTaus = newAuxTaus;
                }
            }
            newTaus.addAll(auxTaus);
        }
        return newTaus;
    }
}
