package Solver.Heuristics;

import Solver.*;

import java.util.ArrayList;
import java.util.List;

public class FirstChoice extends Heuristic{

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

        //  Pick the first unit clause
        Literal l = unitClauses.get(0).getLiterals().get(0);
        AP.remove(l.getProp());
        phi.removeClause(unitClauses.get(0));
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }

    // Choose first AP and boolean value at true
    @Override
    public Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP) {
        Prop p = AP.get(0);
        AP.remove(p);
        Boolean c = true;
        return new Pair<>(p,c);
    }

    @Override
    public String toString() {
        return "first-choice heuristic";
    }

}
