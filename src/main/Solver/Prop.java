package Solver;

import java.util.Objects;

public class Prop implements Form {
    private final String symbol;

    public String getSymbol() {
        return symbol;
    }

    public Prop(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        if (symbol.equals(this.getSymbol())) {
            return new ConstForm(b);
        }
        return new Prop(this.getSymbol());
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        return tau.eval(this);
    }

}
