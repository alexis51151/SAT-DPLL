package Solver.Heuristics;

import Solver.*;

import java.util.*;

public class JeroslowWang extends Heuristic {
    private Random rand;   // Random generator

    public JeroslowWang(int seed) {
        this.rand = new Random(seed);
    }

    public JeroslowWang() {
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
//        int Jmax = 0;
//        Prop Pmax = AP.get(0);
//        for (int i = 0; i < 1; i++) {
//            Prop p = AP.get(i);
//            int J = 0;
//            for (Clause c : p.getClauses()) {
//                J += Math.pow(2,-c.getNbLiterals());
//            }
//            if (J > Jmax) {
//                Jmax = J;
//                Pmax = p;
//            }
//        }
//        return new Pair<>(Pmax, rand.nextBoolean());
//    }

    @Override
    public Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP) {
        HashMap<Literal, Integer> occurrences = new HashMap<>();
        for (Clause clause : phi.getClauses()) {
            List<Literal> literals = clause.getLiterals();
            occurrences.merge(literals.get(0), 1, Integer::sum);
            occurrences.merge(literals.get(1), 1, Integer::sum);
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
        return "Jeroslow-Wang heuristic";
    }

}
