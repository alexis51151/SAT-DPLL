package Solver;

import Solver.Heuristics.Heuristic;
import Solver.Heuristics.RandomChoice;

import java.util.*;

public class DPLL implements SATSolver {
    private final List<Prop> props; // World definition
    private final Random rand = new Random();   // Random generator
    private final Heuristic heuristic;

    public Boolean eval(Form phi, TruthAssignment tau) {
        return phi.eval(tau);
    }

    public static List<Prop> create(List<Prop> p) {
        return new ArrayList<>(p);
    }

    public DPLL(List<Prop> props) {
        this.props = props;
        //  By default, random choice
        this.heuristic = RandomChoice.create(props);
    }

    public DPLL(List<Prop> props, Heuristic heuristic) {
        this.props = props;
        this.heuristic = Heuristic.create();
    }


    public Boolean SAT(Form phi) {
        return solve(phi, DPLL.create(props));
    }

    public TruthAssignment SATTruthAssignement(Form phi) {
        return solve(phi, DPLL.create(props), new TruthAssignment(new HashSet<>()));
    }


    public TruthAssignment solve(Form phi, List<Prop> AP, TruthAssignment tau) {
        // Base cases
        if (AP.size() == 0 || phi instanceof ConstForm || (phi instanceof CNF && ((CNF) phi).nbClauses() == 0)) {
            return eval(phi, new TruthAssignment(new HashSet<>())) ? tau : null;
        }

        assert phi instanceof CNF;
        // Unit-Preference Rule
        Pair<Prop, Boolean> choice = heuristic.unitPreferenceRule((CNF) phi, AP);
        if (choice != null) {
            Prop p = choice.a;
            Boolean b = choice.b;
            Form psi = phi.substitute(p.getSymbol(), b);
            if (b) {
                Set<Prop> new_props = tau.getTau();
                new_props.add(p);
                return solve(psi, DPLL.create(AP), new TruthAssignment(new_props));
            }
            return solve(psi, DPLL.create(AP), tau);
        }

        // Splitting Rule
        choice = heuristic.splittingRule(AP);
        Prop p = choice.a;
        Boolean b = choice.b;

        // 1st option
        Form psi = phi.substitute(p.getSymbol(), b);
        TruthAssignment new_tau = solve(psi, DPLL.create(AP), tau.create(choice));
        if (new_tau != null){
            return new_tau;
        }

        // 2nd option
        choice.b = !choice.b;
        Form theta = phi.substitute(p.getSymbol(), !b);
        return solve(theta, DPLL.create(AP), tau.create(choice));
    }



    public Boolean solve(Form phi, List<Prop> AP) {
        // Base case
        if (AP.size() == 0 || phi instanceof ConstForm) {
            return eval(phi, new TruthAssignment(new HashSet<>()));
        }

        assert phi instanceof CNF;
        // Unit-Preference Rule
        Pair<Prop, Boolean> choice = heuristic.unitPreferenceRule((CNF) phi, AP);
        if (choice != null) {
            Prop p = choice.a;
            Boolean b = choice.b;
            Form psi = phi.substitute(p.getSymbol(), b);
            return solve(psi, DPLL.create(AP));
        }

        // Splitting Rule
        choice = heuristic.splittingRule(AP);
        Prop p = choice.a;
        Boolean b = choice.b;

        // 1st option
        Form psi = phi.substitute(p.getSymbol(), b);
        if (solve(psi, DPLL.create(AP)))
            return true;

        // 2nd option
        Form theta = phi.substitute(p.getSymbol(), !b);
        return solve(theta, DPLL.create(AP));

    }

    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseFirstClause(List<Clause> C, CNF phi, List<Prop> AP) {
        Literal l = C.get(0).getLiterals().get(0);
        AP.remove(l.getProp());
        phi.removeClause(C.get(0));
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }

    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseFirstProp(List<Prop> AP) {
        Prop p = AP.get(0);
        AP.remove(p);
        Boolean c = true;
        return new Pair<>(p,c);
    }


}
