package Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Prop implements Form {
    private final String symbol;
    private Boolean value = null;
    private List<Clause> posClauses = new ArrayList<>();
    private List<Clause> negClauses = new ArrayList<>();


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

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }


    @Override
    public boolean eval(TruthAssignment tau) {
        return tau.eval(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prop prop = (Prop) o;
        return Objects.equals(getSymbol(), prop.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSymbol());
    }

    public void addClause(Clause c, Boolean b) {
        if (b) {
            this.posClauses.add(c);
        } else {
            this.negClauses.add(c);
        }
    }

    public List<Clause> getPosClauses() {
        return posClauses;
    }

    public List<Clause> getNegClauses() {
        return negClauses;
    }

    public void reset() {
        this.value = null;
        this.posClauses = new ArrayList<>();
        this.negClauses = new ArrayList<>();
    }



}
