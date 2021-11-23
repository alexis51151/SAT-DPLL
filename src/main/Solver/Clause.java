package Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clause implements Form {
    private final List<Literal> literals;

    public Clause(List<Literal> literals) {
//        for (Literal l : literals) {
//            l.getProp().addClause(this);
//        }
        this.literals = literals;
    }

    public List<Literal> getLiterals() {
        return literals;
    }

    public int getNbLiterals() {
        return literals.size();
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        // Disjunction of literals
        for (Literal l : literals){
            if (l.eval(tau)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        List<Literal> substitute_literals = new ArrayList<>();
        for (Literal l : literals){
            Form substitute = l.substitute(symbol, b);
            // If a disjunction is true, then the clause becomes true
            if (substitute instanceof ConstForm)  {
                if (((ConstForm) substitute).get()) {
                    return new ConstForm(true);
                } else {
                    continue;
                }
            }
            substitute_literals.add(l);
        }
        if (substitute_literals.size() == 0)
            return new ConstForm(false);    // Empty set of clauses
        return new Clause(substitute_literals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(getLiterals(), clause.getLiterals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLiterals());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(");
        for (Literal literal : literals) {
            s.append(literal.toString()).append(" \\/ ");
        }
        s = new StringBuilder(s.substring(0, s.length() - 3));
        s.append(")");
        return s.toString();
    }
}
