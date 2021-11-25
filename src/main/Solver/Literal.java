package Solver;

import java.util.Objects;

public class Literal implements Form{
    private final Prop prop;
    private final boolean negative;

    public Literal(Prop p, boolean negative) {
        this.prop = p;
        this.negative = negative;
    }

    public Literal(String symbol, boolean negative) {
        this.prop = new Prop(symbol);
        this.negative = negative;
    }

    public Literal negation() {
        return new Literal(prop, !negative);
    }

    public Prop getProp() {
        return prop;
    }


    @Override
    public boolean eval(TruthAssignment tau) {
        return negative != prop.eval(tau);
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        if (symbol.equals(prop.getSymbol())) {
            return new ConstForm(negative != b);
        }
        return new Literal(prop, negative);
    }

    public boolean isNegative() {
        return negative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return isNegative() == literal.isNegative() && Objects.equals(prop, literal.prop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prop, isNegative());
    }

    @Override
    public String toString() {
        if (isNegative()) {
            return "~" + prop.toString();
        }
        return prop.toString();
    }
}
