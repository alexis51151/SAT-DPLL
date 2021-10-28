package Solver;

import java.util.ArrayList;
import java.util.List;

public class CNF implements Form{
    private final List<Form> clauses;

    public CNF(List<Form> clauses) {
        this.clauses = clauses;
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
        List<Form> substituted_clauses = new ArrayList<>();
        for (Form clause : clauses){
            substituted_clauses.add(clause.substitute(symbol, b));
        }
        return new CNF(substituted_clauses);
    }
}
