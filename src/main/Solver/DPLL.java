package Solver;

import FormulaGenerator.RandomGenerator;
import Solver.Heuristics.*;

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
        this.heuristic = new RandomChoice();
    }

    public DPLL(List<Prop> props, Heuristic heuristic) {
        this.props = props;
        this.heuristic = heuristic;
    }


    /**
     * Satisfiability wrapper function.
     * @param phi   Formula to satisfy.
     * @return      Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public TruthAssignment SAT(CNF phi) {
        return solve(phi, DPLL.create(props), new TruthAssignment(new HashSet<>()));
    }



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
        TruthAssignment new_tau = solve(psi, DPLL.create(unassigned), tau.create(choice));
        if (new_tau != null){
            return new_tau;
        }
        // 2nd option
        choice.b = !choice.b;
        Form theta = phi.substitute(p.getSymbol(), !b);
        tau.add(choice);
        return solve(theta, unassigned, tau);
    }


    public static void main(String[] args) {
        RandomGenerator g = new RandomGenerator(40,60);
        CNF cnf = g.generate3SAT();
        DPLL solver = new DPLL(g.getProps(), new JeroslowWang());
        solver.SAT(cnf);
    }

}
