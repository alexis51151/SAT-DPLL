package Solver;

public class NotForm implements Form {
    private final Form Not;

    public NotForm(Form not) {
        Not = not;
    }

    @Override
    public String toString() {
        return "(~ " + Not.toString() + " )";
    }

    @Override
    public Form substitute(String symbol, Boolean b) {
        return new NotForm(Not.substitute(symbol, b));
    }

    @Override
    public boolean eval(TruthAssignment tau) {
        return !Not.eval(tau);
    }
}
