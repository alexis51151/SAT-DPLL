package Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public Boolean solve(Form phi, List<Prop> AP) {
        // Base case
        if (AP.size() == 0 || phi instanceof ConstForm) {
            return eval(phi, new TruthAssignment(new ArrayList<>()));
        }

        assert phi instanceof CNF;
        // Unit-Preference Rule
        Pair<Prop, Boolean> choice = unitPreferenceRule((CNF) phi);
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

    public Pair<Prop, Boolean> unitPreferenceRule(CNF phi) {
        List<Clause> unitClauses = new ArrayList<>();
        for (Clause clause : phi.getClauses()) {
            if (clause.getNbLiterals() == 1)
                unitClauses.add(clause);
        }

        if (unitClauses.size() == 0){
            return null;
        }
        return chooseRandomClause(unitClauses, phi);
    }

    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseRandomClause(List<Clause> C, CNF phi) {
        Clause clause = C.get(rand.nextInt(C.size()));
        phi.removeClause(clause);
        Literal l = clause.getLiterals().get(0);
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

}
