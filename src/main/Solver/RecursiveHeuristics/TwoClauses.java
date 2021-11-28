package Solver.RecursiveHeuristics;

import Solver.*;

import java.util.*;

public class TwoClauses extends Heuristic{
    private final Random rand;   // Random generator

    public TwoClauses() {
        this.rand = new Random();
    }

    public TwoClauses(int seed) {
        this.rand = new Random(seed);
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

    @Override
    public Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP) {
        //  Find 2-clauses and max occurrences
        HashMap<Literal, Integer> occurrences = new HashMap<>();
        for (Clause clause : phi.getClauses()) {
            if (clause.getNbLiterals() == 2) {
                List<Literal> literals = clause.getLiterals();
                occurrences.merge(literals.get(0), 1, Integer::sum);
                occurrences.merge(literals.get(1), 1, Integer::sum);
            }
        }

        // If there are no 2-clauses, we do the random heuristic
        if (occurrences.size() == 0) {
            Prop p = AP.get(rand.nextInt(AP.size()));
            AP.remove(p);
            Boolean c = rand.nextBoolean();
            return new Pair<>(p,c);
        }

        //  Find literals with max occurences
        List<Literal> maxLiterals = new ArrayList<>();
        int max = 0;
        for (Map.Entry<Literal, Integer> entry : occurrences.entrySet()) {
            int val = entry.getValue();
            if (val > max){
                maxLiterals = new ArrayList<>(List.of(entry.getKey()));
                max = val;
            } else if (val == max) {
                maxLiterals.add(entry.getKey());
            }
        }

        // Randomly break tie
        Literal l = maxLiterals.get(rand.nextInt(maxLiterals.size()));
        AP.remove(l.getProp());
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }

    @Override
    public String toString() {
        return "2-clause heuristic";
    }

}
