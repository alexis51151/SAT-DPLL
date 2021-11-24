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

    /**
     * Satisfiability with truth assignement solver function.
     * @param phi   Formula to satisfy.
     * @return Truth assignement that satisfies phi if phi is satisfiable; null otherwise.
     */
    public HashMap<Prop, Boolean> solve_iter(CNF phi) {
        List<Prop> unassigned = this.props;
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
            Pair<Prop, Boolean>  choice = splittingRule(unassigned);
            deductions = unitPropagation(phi, choice, unassigned);
            assignments.add(new Assignment(deductions.a, choice, false));

            // Handle conflicts
            while (!deductions.b) {
                Assignment a = assignments.pop();
                while (a.flipped) {
                    backtrack(a, unassigned);
                    if (assignments.size() == 0) {
                        return null;
                    }
                    a = assignments.pop();
                }
                backtrack(a, unassigned);
                a.choice.b = !a.choice.b;
                deductions = unitPropagation(phi, a.choice, unassigned);
                assignments.add(new Assignment(deductions.a, choice, true));
            }
        }

        while (assignments.size() > 0) {
            Assignment a = assignments.pop();
            tau.put(a.choice.a, a.choice.b);
            tau.putAll(a.tau);
        }
        return tau;
    }

    public Pair<HashMap<Prop, Boolean>, Boolean>  unitPropagation(CNF phi, Pair<Prop, Boolean> choice, List<Prop> unassigned) {
        // Conflict flag
        boolean conflict = false;
        // HashMap of all the deductions made
        HashMap<Prop, Boolean> deductions = new HashMap<>();
        // Stack of all the new choices to propagate
        Stack<Pair<Prop, Boolean>> stack = new Stack<>();
        // New unit Choices
        List<Literal> unitClauses = new ArrayList<>();

        if (choice != null) {
            stack.push(choice);
        }

        // Also add the unit choices into the stack (useful if it's the first unitPropagation)
        Iterator<Literal> itr = phi.getUnitClauses().iterator();
        while (itr.hasNext()) {
            Literal l = itr.next();
            deductions.put(l.getProp(), !l.isNegative());
            stack.push(new Pair<>(l.getProp(), !l.isNegative()));
            itr.remove();
            unassigned.remove(l.getProp());
        }

        // Big loop to propagate the changes and find unit clauses
        while (stack.size() > 0 && !conflict) {
            // Update unitClauses
            choice = stack.pop();
            Prop p = choice.a;
            Boolean b = choice.b;
            List<Clause> clauses;

            if (b) {
                clauses = p.getNegClauses();
            } else {
                clauses = p.getPosClauses();
            }

            for (Clause c : clauses) {
                assert c.getNbLiterals() <= 3;
                // Conflict
                if (c.getNbLiterals() == 1) {
                    conflict = true;
                    break;
                }
                // new unit clauses
                if (c.getNbLiterals() == 2) {
                    List<Literal> l = c.getLiterals();
                    Literal lit = new Literal(p, b);
                    if (l.get(0).equals(lit)) {
                        l.remove(lit);
                        l.add(l.size(), lit);
                    }
                    unitClauses.add(c.getFirstLiteral());
                }
                // new 2-clauses
                if (c.getNbLiterals() == 3) {
                    List<Literal> l = c.getLiterals();
                    Literal lit = new Literal(p, b);
                    if (l.get(0).equals(lit) || l.get(1).equals(lit) ) {
                        l.remove(lit);
                        l.add(l.size(), lit);
                    }
                }
                c.decrNbLiterals();
            }

            // Look at all unit clauses for conflict
            itr = unitClauses.iterator();
            while (itr.hasNext()) {
                Literal l = itr.next();
                if (deductions.containsKey(l.getProp())) {
                    if (deductions.get(l.getProp()) == l.isNegative()) {
                        conflict = true;
                        break;
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

        if (conflict) {
            while (stack.size() > 0) {
                choice = stack.pop();
                // Remove the deductions that were not explored
                deductions.remove(choice.a);
            }
            return new Pair<>(deductions, false);
        }

        return new Pair<>(deductions, true);
    }

    public Pair<Prop, Boolean> splittingRule(List<Prop> unassigned) {
        int index = rand.nextInt(unassigned.size());
        Prop p = unassigned.get(index);
        unassigned.remove(index);
        return new Pair<>(p, rand.nextBoolean());
    }

    public void backtrack(Assignment a, List<Prop> unassigned) {
        HashMap<Prop, Boolean> deductions = a.tau;
        Pair<Prop, Boolean> choice = a.choice;
        // Modify the nbLiterals in clauses of the chosen var
        Prop p = choice.a;
        Boolean b = choice.b;
        List<Clause> clauses;
        if (b) {
            clauses = p.getNegClauses();
        } else {
            clauses = p.getPosClauses();
        }
        for (Clause c : clauses) {
            if (c.getNbLiterals() == 1) {
                break;
            }
            c.incrNbLiterals();
        }

        for (Map.Entry<Prop, Boolean> deduction : deductions.entrySet()) {
            p = deduction.getKey();
            b = deduction.getValue();
            // put p back in the unassigned list
            unassigned.add(p);
            if (b) {
                clauses = p.getNegClauses();
            } else {
                clauses = p.getPosClauses();
            }
            for (Clause c : clauses) {
                if (c.getNbLiterals() == 1) {
                    break;
                }
                c.incrNbLiterals();
            }
        }
    }



    public static void main(String[] args) {
        RandomGenerator g = new RandomGenerator(40,60);
        CNF cnf = g.generate3SAT();
        DPLL solver = new DPLL(g.getProps(), new JeroslowWang());
        solver.SAT(cnf);
    }

}
