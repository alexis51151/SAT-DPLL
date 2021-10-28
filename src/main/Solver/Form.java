package Solver;

public interface Form {

    public boolean eval(TruthAssignment tau);
    public String toString();
    public Form substitute(String symbol, Boolean b);
}
