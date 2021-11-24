package Solver;

import java.util.HashMap;
import java.util.Objects;

public class Assignment {

    public HashMap<Prop, Boolean> tau;
    public Pair<Prop, Boolean> choice;
    public boolean flipped = false;

    public Assignment(HashMap<Prop, Boolean> tau, Pair<Prop, Boolean> choice, boolean flipped) {
        this.tau = tau;
        this.choice = choice;
        this.flipped = flipped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(tau, that.tau);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tau);
    }

}
