package Solver.RecursiveHeuristics;

import Solver.CNF;
import Solver.Pair;
import Solver.Prop;

import java.util.List;

public abstract class Heuristic {

    public abstract Pair<Prop, Boolean> unitPreferenceRule(CNF phi, List<Prop> AP);

    public abstract Pair<Prop, Boolean> splittingRule(CNF phi, List<Prop> AP);

}
