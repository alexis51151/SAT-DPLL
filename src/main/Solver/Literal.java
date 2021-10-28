package Solver;

import java.util.Objects;

public class Literal implements Form{
    private final Prop p;
    private final boolean negative;

    public Literal(Prop p, boolean negative) {
        this.p = p;
        this.negative = negative;
    }

    public Literal(String symbol, boolean negative) {
        this.p = new Prop(symbol);
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
        return new Literal(p, negative);
    }

    public boolean isNegative() {
        return negative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return isNegative() == literal.isNegative() && Objects.equals(p, literal.p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, isNegative());
    }
}
