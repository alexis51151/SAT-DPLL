package Solver.Heuristics;

import Solver.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomChoice extends Heuristic {

    private final Random rand;   // Random generator
    List<Prop> AP;

    public RandomChoice() {
        this.rand = new Random();
    }

    public RandomChoice(int seed) {
        this.rand = new Random(seed);
    }

    public static RandomChoice create(List<Prop> AP, int seed) {
        return new RandomChoice(seed);
    }

    public static RandomChoice create(List<Prop> AP) {
        return new RandomChoice();
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

    // Random choice of AP and boolean value
    @Override
    public Pair<Prop, Boolean> splittingRule(List<Prop> AP) {
        Prop p = AP.get(rand.nextInt(AP.size()));
        AP.remove(p);
        Boolean c = rand.nextBoolean();
        return new Pair<>(p,c);
    }

}
