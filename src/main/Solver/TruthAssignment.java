package Solver;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Only used for the Recursive solver. For the iterative solve, I use an HashMap instead.
 */
public class TruthAssignment {
    private Set<Prop> tau; // List of props that are evaluated as 1

    public TruthAssignment(Set<Prop> tau) {
        this.tau = tau;
    }

    public Set<Prop> getTau() {
        return tau;
    }

    public Boolean eval(Prop p) {
        return tau.contains(p);
    }

    public Boolean contains(Prop p){
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        for (Prop p : tau) {
            str.append(p.getSymbol()).append(",");
        }
        str.deleteCharAt(str.length()-1);
        str.append("}");
        return str.toString();
    }

    public TruthAssignment create(Pair<Prop, Boolean> choice) {
        Set<Prop> props = new HashSet<>(tau);
        Prop p = choice.a;
        Boolean b = choice.b;
        if (b) {
            props.add(p);
        }
        return new TruthAssignment(props);
    }

    public void add(Pair<Prop, Boolean> choice) {
        Prop p = choice.a;
        Boolean b = choice.b;
        if (b) {
            tau.add(p);
        }
    }
}

