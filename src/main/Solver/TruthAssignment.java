package Solver;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TruthAssignment {
    private final Set<Prop> tau; // List of props that are evaluated as 1

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
}

