package Solver;
import java.util.List;
import java.util.Objects;

public class TruthAssignment {
    private final List<Prop> tau; // List of props that are evaluated as 1

    public TruthAssignment(List<Prop> tau) {
        this.tau = tau;
    }

    public Boolean eval(Prop p) {
        return tau.contains(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruthAssignment that = (TruthAssignment) o;
        return Objects.equals(tau, that.tau);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tau);
    }
}

