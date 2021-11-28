package Solver;

import FormulaGenerator.RandomGenerator;
import Solver.RecursiveHeuristics.*;

import java.util.*;

public class DPLLRecursive implements SATSolver {
    private final List<Prop> props; // World definition
    private final Heuristic heuristic;
    public int nbCalls = 0;

    public Boolean eval(Form phi, TruthAssignment tau) {
        return phi.eval(tau);
    }

    public static List<Prop> create(List<Prop> p) {
        return new ArrayList<>(p);
    }

    public DPLLRecursive(List<Prop> props) {
        this.props = props;
        //  By default, random choice
        this.heuristic = new RandomChoice();
    }

    public DPLLRecursive(List<Prop> props, Heuristic heuristic) {
        this.props = props;
        this.heuristic = heuristic;
    }


    /**
     * Satisfiability wrapper function.
     * @param phi   Formula to satisfy.
     * @return      Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public TruthAssignment SAT(CNF phi) {
        return solve(phi, DPLLRecursive.create(props), new TruthAssignment(new HashSet<>()));
    }

    /**
     * Satisfiability with truth assignement solver function.
     * @param phi   Formula to satisfy.
     * @param unassigned    Set of unassigned propositions in phi.
     * @param tau   Truth assignment constructed.
     * @return Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public TruthAssignment solve(Form phi, List<Prop> unassigned, TruthAssignment tau) {
        // Base cases
        if (unassigned.size() == 0 || phi instanceof ConstForm || (phi instanceof CNF && ((CNF) phi).nbClauses() == 0)) {
            return eval(phi, new TruthAssignment(new HashSet<>())) ? tau : null;
        }

        assert phi instanceof CNF;
        // Unit-Preference Rule
        Pair<Prop, Boolean> choice = heuristic.unitPreferenceRule((CNF) phi, unassigned);
        if (choice != null) {
            Prop p = choice.a;
            Boolean b = choice.b;
            Form psi = phi.substitute(p.getSymbol(), b);
            if (b) {
                Set<Prop> new_props = tau.getTau();
                new_props.add(p);
                return solve(psi, unassigned, new TruthAssignment(new_props));
            }
            return solve(psi, unassigned, tau);
        }

        // Splitting Rule
        choice = heuristic.splittingRule((CNF) phi, unassigned);
        Prop p = choice.a;
        Boolean b = choice.b;

        // 1st option
        Form psi = phi.substitute(p.getSymbol(), b);
        TruthAssignment new_tau = solve(psi, DPLLRecursive.create(unassigned), tau.create(choice));
        if (new_tau != null){
            return new_tau;
        }
        // 2nd option
        choice.b = !choice.b;
        Form theta = phi.substitute(p.getSymbol(), !b);
        tau.add(choice);
        return solve(theta, unassigned, tau);
    }


}
