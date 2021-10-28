package Solver;

import java.util.Objects;

public class ConstForm implements Form{
    private final Boolean val;

    public ConstForm(Boolean val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return (val) ? "1" : "0";
    }

    public Boolean get() {
        return val;
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        return val;
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        return new ConstForm(this.val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstForm constForm = (ConstForm) o;
        return Objects.equals(val, constForm.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
