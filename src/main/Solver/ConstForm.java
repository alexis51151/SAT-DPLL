package Solver;

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
}
