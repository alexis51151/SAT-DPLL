package Solver.Heuristics;

import Solver.CNF;
import Solver.Pair;
import Solver.Prop;

import java.util.List;

public abstract class Heuristic {

    public abstract Pair<Prop, Boolean> splittingRule(List<Prop> AP);

}
