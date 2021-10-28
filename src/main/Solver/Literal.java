package Solver;

public class Literal implements Form{
    private final Prop p;
    private final boolean negative;

    public Literal(Prop p, boolean negative) {
        this.p = p;
        this.negative = negative;
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        return negative != p.eval(tau);
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        if (symbol.equals(p.getSymbol())) {
            return new ConstForm(negative != b);
        }
        return this;
    }

    public boolean isNegative() {
        return negative;
    }
}
