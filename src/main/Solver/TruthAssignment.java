package Solver;
import java.util.List;

public class TruthAssignment {
    private final List<Prop> tau; // List of props that are evaluated as 1

    public TruthAssignment(List<Prop> tau) {
        this.tau = tau;
    }

    public Boolean eval(Prop p) {
        return tau.contains(p);
    }
}
