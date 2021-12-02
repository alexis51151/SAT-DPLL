package Solver.Heuristics;

import Solver.CNF;
import Solver.Literal;
import Solver.Pair;
import Solver.Prop;

import java.util.*;

public class JeroslowWang extends Heuristic {
    public Random rand = new Random(1L); // To break ties

    @Override
    public Pair<Prop, Boolean> splittingRule(List<Prop> unassigned) {
        HashMap<Literal, Integer> occurrences = new HashMap<>();
        for (Prop p: unassigned) {
            int valPos = p.getPosClauses().size();
            int valNeg = p.getNegClauses().size();
            if (valPos >= valNeg) {
                occurrences.put(new Literal(p, false), valPos);
            } else {
                occurrences.put(new Literal(p, true), valNeg);
            }
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
        return "Jeroslow-Wang";
    }


}
