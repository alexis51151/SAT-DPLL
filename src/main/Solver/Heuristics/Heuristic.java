package Solver.Heuristics;

import Solver.CNF;
import Solver.Pair;
import Solver.Prop;

import java.util.List;

public abstract class Heuristic {

    public static RandomChoice create() {
        throw new ExceptionInInitializerError("Cannot create an Heuristic class!");
    }

    public abstract Pair<Prop, Boolean> unitPreferenceRule(CNF phi, List<Prop> AP);

    public abstract Pair<Prop, Boolean> splittingRule(List<Prop> AP);

}
