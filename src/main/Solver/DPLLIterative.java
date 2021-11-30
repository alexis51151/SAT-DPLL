package Solver;

import Solver.Heuristics.*;

import java.util.*;

public class DPLLIterative implements SATSolver {
    private final List<Prop> props; // World definition
    private final Heuristic heuristic;
    private int nbCalls = 0;

    public DPLLIterative(List<Prop> props) {
        this.props = props;
        //  By default, random choice
        this.heuristic = new JeroslowWang();
    }

    public DPLLIterative(List<Prop> props, Heuristic heuristic) {
        this.props = props;
        this.heuristic = heuristic;
    }

    /**
     * Satisfiability wrapper function.
     * @param phi   Formula to satisfy.
     * @return      Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public HashMap<Prop, Boolean> SAT(CNF phi) {
        return solve(phi);
    }


    /**
     * Satisfiability with truth assignement solver function.
     * @param phi   Formula to satisfy.
     * @return Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public HashMap<Prop, Boolean> solve(CNF phi) {
        this.nbCalls = 0; // Variable to track the nb of DPLL calls (i.e. calls to the Splitting rule)
        List<Prop> unassigned = new ArrayList<>(this.props);
        Stack<Assignment> assignments = new Stack<>();
        //  Simplify the unit clauses present at the start
        Pair<HashMap<Prop, Boolean>, Boolean> deductions = unitPropagation(phi, null, unassigned);
        HashMap<Prop, Boolean> tau = new HashMap<>(deductions.a);

        if (!deductions.b) {
            return null;
        } else if (unassigned.size() == 0) {
            return deductions.a;
        }

        // Assign all the atomic propositions
        while (unassigned.size() != 0) {
            // Make a new decision
            this.nbCalls++;  // New DPLL call
            Pair<Prop, Boolean>  choice = heuristic.splittingRule(unassigned);
            deductions = unitPropagation(phi, choice, unassigned);
            assignments.add(new Assignment(deductions.a, choice, false));

            // Handle conflicts
            while (!deductions.b) {
                Assignment a = assignments.pop();
                while (a.flipped) {
                    // unassign the flipped choice
                    a.choice.a.setValue(null);
                    unassigned.add(a.choice.a);
                    backtrack(a, unassigned);
                    if (assignments.size() == 0) {
                        return null;
                    }
                    a = assignments.pop();
                }
                backtrack(a, unassigned);
                a.choice.b = !a.choice.b;
                a.choice.a.setValue(a.choice.b);
                deductions = unitPropagation(phi, a.choice, unassigned);
                assignments.add(new Assignment(deductions.a, a.choice, true));
            }
        }

        // Retrieve the truth assignment
        while (assignments.size() > 0) {
            Assignment a = assignments.pop();
            tau.put(a.choice.a, a.choice.b);
            tau.putAll(a.tau);
        }
        return tau;
    }

    public Pair<HashMap<Prop, Boolean>, Boolean>  unitPropagation(CNF phi, Pair<Prop, Boolean> choice, List<Prop> unassigned) {
        Stack<Pair<Prop, Boolean>> stack = new Stack<>();
        // HashMap of all the new deductions
        HashMap<Prop, Boolean> deductions = new HashMap<>();
        // New unit Choices
        List<Literal> unitClauses = new ArrayList<>();

        if (choice != null) {
            stack.push(choice);
        } else {
            Iterator<Literal> itr = phi.getUnitClauses().iterator();
            while (itr.hasNext()) {
                Literal l = itr.next();
                deductions.put(l.getProp(), !l.isNegative());
                stack.push(new Pair<>(l.getProp(), !l.isNegative()));
                unassigned.remove(l.getProp());
                itr.remove();
            }
        }

        while (stack.size() != 0) {
            Pair<Prop, Boolean> elt = stack.pop();
            Prop p = elt.a;
            Boolean b = elt.b;
            p.setValue(b);

            List<Clause> clauses;
            if (b) {
                clauses = p.getNegClauses();
            } else {
                clauses = p.getPosClauses();
            }

            for (Clause c : clauses) {
                List<Literal> notFalseLiterals = new ArrayList<>();
                // Nb of unassigned literals must be > 0
                for (Literal l : c.getLiterals()) {
                    if(l.getProp().getValue() == null || (l.getProp().getValue() != l.isNegative())) {
                        notFalseLiterals.add(l);
                    }
                }
                if (notFalseLiterals.size() == 0) {
                    return new Pair<>(deductions, false);
                }
                if (notFalseLiterals.size() == 1) {
                    unitClauses.add(notFalseLiterals.get(0));
                }
            }

            // Look at all unit clauses for conflict
            Iterator<Literal> itr = unitClauses.iterator();
            while (itr.hasNext()) {
                Literal l = itr.next();
                if (l.getProp().getValue() != null ) {
                    if (l.getProp().getValue()  == l.isNegative()) {
                        return new Pair<>(deductions, false);
                    } else {
                        // We already have deduced that earlier
                        itr.remove();
                        continue;
                    }
                }
                deductions.put(l.getProp(), !l.isNegative());
                if (unassigned.contains(l.getProp())) {
                    stack.push(new Pair<>(l.getProp(), !l.isNegative()));
                    unassigned.remove(l.getProp());
                }
                itr.remove();
            }
        }
        return new Pair<>(deductions, true);
    }

    public void backtrack(Assignment a, List<Prop> unassigned) {
        for (Map.Entry<Prop, Boolean> deduction : a.tau.entrySet()) {
            Prop p = deduction.getKey();
            unassigned.add(p);
            p.setValue(null);
        }
    }

    public int getNbCalls() {
        return nbCalls;
    }
}
