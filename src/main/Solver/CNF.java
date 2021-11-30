package Solver;

import java.util.*;

public class CNF implements Form{
    private List<Clause> clauses;
    private List<Literal> unitClauses = new ArrayList<>();

    public CNF(List<Clause> clauses) {
        for (Clause c : clauses) {
            // Add a pointer to unitClauses for all clauses
            c.setUnitClauses(unitClauses);
            // If a clause is initially unit, add the Literal to unitClauses
            if (c.getNbLiterals() == 1) {
                unitClauses.add(c.getFirstLiteral());
            }
        }
        this.clauses = clauses;
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public int nbClauses() { return clauses.size(); }

    public void addAll(CNF cnf) {
        List<Clause> newClauses = new ArrayList<>(clauses);
        newClauses.addAll(cnf.getClauses());
        this.clauses = newClauses;
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        for (Form clause : clauses){
            if (!clause.eval(tau))
                return false;
        }
        return true;
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        List<Clause> substituted_clauses = new ArrayList<>();
        for (Clause clause : clauses) {
            Form substitute = clause.substitute(symbol, b);
            assert (substitute instanceof ConstForm || substitute instanceof Clause);
            if (substitute instanceof ConstForm){
                if (!((ConstForm) substitute).get())
                return substitute;
            } else {
                substituted_clauses.add((Clause) substitute);
            }
        }
        return new CNF(substituted_clauses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CNF cnf = (CNF) o;
        return Objects.equals(clauses, cnf.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauses);
    }

    public void removeClause(Clause clause) {
        clauses.remove(clause);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Clause clause : clauses) {
            s.append(clause.toString()).append("\n /\\ ");
        }
        return s.substring(0, s.length() - 3);
    }

    public List<Literal> getUnitClauses() {
        return unitClauses;
    }

    public String toDimacs() {
        StringBuilder result = new StringBuilder();
        Set<Prop> props = new HashSet<>();
        for (Clause c : clauses) {
            StringBuilder cs = new StringBuilder();
            for (Literal lit : c.getLiterals()) {
                props.add(lit.getProp());
                String val = lit.getProp().getSymbol().substring(1);
                if (lit.isNegative()) {
                    cs.append("-");
                }
                cs.append(val);
                cs.append(" "); // space delimitor
            }
            cs.append("0\n"); // end of clause delimitor
            result.append(cs);
        }
        String problemLine = "p cnf " + props.size() + " " + clauses.size() + "\n";
        return problemLine + result;
    }
}
