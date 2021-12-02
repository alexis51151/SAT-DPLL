package Solver.Heuristics;

import Solver.Clause;
import Solver.Literal;
import Solver.Pair;
import Solver.Prop;

import java.util.*;

public class TwoClauses extends Heuristic{
    public Random rand = new Random(1L); // To break ties

    public Pair<Literal, Literal> getTwoClausesLiterals(Clause c) {
        List<Literal> l = new ArrayList<>();
        for (Literal lit : c.getLiterals()) {
            if (lit.getProp().getValue() == null) {
                l.add(lit);
            }
        }
        if (l.size() == 2) {
            return new Pair<>(l.get(0), l.get(1));
        }
        return null;
    }


    @Override
    public Pair<Prop, Boolean> splittingRule(List<Prop> unassigned) {
        //  Find 2-clauses and max occurrences
        HashMap<Literal, Integer> occurrences = new HashMap<>();
        for (Prop p : unassigned) {
            for (Clause c : p.getNegClauses()) {
                Pair<Literal, Literal> pair = getTwoClausesLiterals(c);
                if (pair != null) {
                    List<Literal> literals = c.getLiterals();
                    occurrences.merge(pair.a, 1, Integer::sum);
                    occurrences.merge(pair.b, 1, Integer::sum);
                }
            }
            for (Clause c : p.getPosClauses()) {
                Pair<Literal, Literal> pair = getTwoClausesLiterals(c);
                if (pair != null) {
                    List<Literal> literals = c.getLiterals();
                    occurrences.merge(pair.a, 1, Integer::sum);
                    occurrences.merge(pair.b, 1, Integer::sum);
                }
            }
        }

        // If there are no 2-clauses, we do the random heuristic
        if (occurrences.size() == 0) {
            int index = rand.nextInt(unassigned.size());
            Prop p = unassigned.get(index);
            unassigned.remove(index);
            return new Pair<>(p, rand.nextBoolean());
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
        unassigned.remove(l.getProp());
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }

    @Override
    public String toString() {
        return "TwoClauses";
    }

}
