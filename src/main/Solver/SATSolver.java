package Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SATSolver {
    private final List<Prop> props;   // Definition of the world
    private final Random rand = new Random();   // Random generator

    public Boolean eval(Form phi, TruthAssignment tau) {
        return phi.eval(tau);
    }

    public static List<Prop> create(List<Prop> p) {
        return new ArrayList<>(p);
    }

    public SATSolver(List<Prop> props) {
        this.props = props;
    }

    public Boolean SAT(Form phi) {
        return solve(phi, SATSolver.create(props));
    }

    public Boolean solve(Form phi, List<Prop> AP) {
        if (AP.size() == 0) {
            TruthAssignment tau = new TruthAssignment(new ArrayList<>());   // Any truth assignment, it does not matter here
            phi = new ConstForm(eval(phi, tau));    // We have evaluated all props, just need to convert to a ConstForm
        }
        if (phi instanceof ConstForm){
            return ((ConstForm) phi).get();
        }

        Pair<Prop, Boolean> choice = chooseRandom(AP);
        Prop p = choice.a;
        Boolean b = choice.b;

        // The two possibilities to test according to Lemma 1 (cf lecture 9)
        Form psi = phi.substitute(p.getSymbol(), b);
        if (solve(psi, SATSolver.create(AP)))
            return true;
        Form theta = phi.substitute(p.getSymbol(), !b);
        return solve(theta, SATSolver.create(AP));
    }

    // Random choice of AP and boolean value
    public Pair<Prop, Boolean> chooseRandom(List<Prop> AP) {
        Prop p = AP.get(rand.nextInt(AP.size()));
        AP.remove(p);
        Boolean c = rand.nextBoolean();
        return new Pair<>(p,c);
    }


}
