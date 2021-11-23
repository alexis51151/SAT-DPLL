package Solver.Heuristics;

import Solver.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DLIS extends Heuristic {
    private final Random rand;   // Random generator

    public DLIS(Random rand) {
        this.rand = rand;
    }

    public DLIS() {
        this.rand = new Random();
    }



    @Override
    public Pair<Prop, Boolean> unitPreferenceRule(CNF phi, List<Prop> AP) {
        //  Find unit clauses
        List<Clause> unitClauses = new ArrayList<>();
        for (Clause clause : phi.getClauses()) {
            if (clause.getNbLiterals() == 1)
                unitClauses.add(clause);
        }

        if (unitClauses.size() == 0){
            return null;
        }

        //  Pick one clause randomly
        Clause clause = unitClauses.get(rand.nextInt(unitClauses.size()));
        phi.removeClause(clause);
        Literal l = clause.getLiterals().get(0);
        AP.remove(l.getProp());
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }

//    @Override
//    public Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP) {
//        Pair<Prop, Integer> max_positive = new Pair<>(AP.get(0), 0);
//        Pair<Prop, Integer> max_negative = new Pair<>(AP.get(0), 0);
//        for (Prop p : AP) {
//            int pos = 0;
//            int neg = 0;
//            for (Clause c : phi.getClauses()) {
//                if (c.getLiterals().contains(new Literal(p, false))) {
//                    pos++;
//                } else if (c.getLiterals().contains(new Literal(p, true))) {
//                    neg++;
//                }
//            }
//            if (pos > max_positive.b) {
//                max_positive = new Pair<>(p, pos);
//            }
//            if (pos > max_negative.b) {
//                max_negative = new Pair<>(p, neg);
//            }
//        }
//        if (max_positive.b > max_negative.b) {
//            return new Pair<>(max_positive.a, true);
//        } else {
//            return new Pair<>(max_negative.a, false);
//        }
//    }

    @Override
    public Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP) {
        Pair<Prop, Integer> max_positive = new Pair<>(AP.get(0), 0);
        Pair<Prop, Integer> max_negative = new Pair<>(AP.get(0), 0);
        for (Prop p : AP) {
            int pos = 0;
            int neg = 0;
            for (Clause c : phi.getClauses()) {
                if (c.getLiterals().contains(new Literal(p, false))) {
                    pos++;
                } else if (c.getLiterals().contains(new Literal(p, true))) {
                    neg++;
                }
            }
            if (pos > max_positive.b) {
                max_positive = new Pair<>(p, pos);
            }
            if (pos > max_negative.b) {
                max_negative = new Pair<>(p, neg);
            }
        }
        if (max_positive.b > max_negative.b) {
            return new Pair<>(max_positive.a, true);
        } else {
            return new Pair<>(max_negative.a, false);
        }
    }

}
