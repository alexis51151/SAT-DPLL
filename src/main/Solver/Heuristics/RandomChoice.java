package Solver.Heuristics;

import Solver.Pair;
import Solver.Prop;

import java.util.List;
import java.util.Random;

public class RandomChoice extends Heuristic {
    public Random rand = new Random();



    public Pair<Prop, Boolean> splittingRule(List<Prop> unassigned) {
        int index = rand.nextInt(unassigned.size());
        Prop p = unassigned.get(index);
        unassigned.remove(index);
        return new Pair<>(p, rand.nextBoolean());
    }


}
