package Solver;

import java.util.*;

public class DPLL implements SATSolver {
    private final List<Prop> props; // World definition
    private final Random rand = new Random();   // Random generator

    public Boolean eval(Form phi, TruthAssignment tau) {
        return phi.eval(tau);
    }

    public static List<Prop> create(List<Prop> p) {
        return new ArrayList<>(p);
    }

    public DPLL(List<Prop> props) {
        this.props = props;
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
        Pair<Prop, Boolean> choice = unitPreferenceRule((CNF) phi, AP);
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
        choice = chooseRandom(AP);
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
        Pair<Prop, Boolean> choice = unitPreferenceRule((CNF) phi, AP);
        if (choice != null) {
            Prop p = choice.a;
            Boolean b = choice.b;
            Form psi = phi.substitute(p.getSymbol(), b);
            return solve(psi, DPLL.create(AP));
        }

        // Splitting Rule
        choice = chooseRandom(AP);
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

    public Pair<Prop, Boolean> unitPreferenceRule(CNF phi, List<Prop> AP) {
        List<Clause> unitClauses = new ArrayList<>();
        for (Clause clause : phi.getClauses()) {
            if (clause.getNbLiterals() == 1)
                unitClauses.add(clause);
        }

        if (unitClauses.size() == 0){
            return null;
        }
        return chooseRandomClause(unitClauses, phi, AP);
//        return chooseFirstClause(unitClauses, phi, AP);
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
    public Pair<Prop, Boolean> chooseRandomClause(List<Clause> C, CNF phi, List<Prop> AP) {
        Clause clause = C.get(rand.nextInt(C.size()));
        phi.removeClause(clause);
        Literal l = clause.getLiterals().get(0);
        AP.remove(l.getProp());
        if (l.isNegative())
            return new Pair<>(l.getProp(), false);
        return new Pair<>(l.getProp(), true);
    }


    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseRandom(List<Prop> AP) {
        Prop p = AP.get(rand.nextInt(AP.size()));
        AP.remove(p);
        Boolean c = rand.nextBoolean();
        return new Pair<>(p,c);
    }

    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseFirstProp(List<Prop> AP) {
        Prop p = AP.get(0);
        AP.remove(p);
        Boolean c = true;
        return new Pair<>(p,c);
    }


}
