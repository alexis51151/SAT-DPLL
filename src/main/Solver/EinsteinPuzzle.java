package Solver;

import java.util.*;

public class EinsteinPuzzle {
    private final CNF cnf;
    private final List<Prop> AP = new ArrayList<>();

    public EinsteinPuzzle() {
        List<Clause> clauses = new ArrayList<>();
        int N = 5;
        // Names for APs
        Set<String> sets = new HashSet<>(Arrays.asList("n", "h", "b", "c", "p"));
        // Basic problem
        for (String prefix : sets) {
            // Each house has at least one _
            for (int i = 1; i <= N; i++) {
                List<Literal> set = new ArrayList<>();
                for (int j = 1; j <= N; j++) {
                    String symbol = prefix + i + j;
                    set.add(new Literal(symbol, false));
                    AP.add(new Prop(symbol));
                }
                clauses.add(new Clause(set));
            }
            // Each house has at most one _
            for (int i = 1; i <= N; i++) {
                for (int k = 1; k <= N; k++) {
                    for (int j = 1; j < k; j++) {
                        List<Literal> newSet = new ArrayList<>();
                        newSet.add(new Literal(prefix + Integer.toString(i) + Integer.toString(j
                        ), true));
                        newSet.add(new Literal(prefix + Integer.toString(i) + Integer.toString(k
                        ), true));
                        clauses.add(new Clause(newSet));
                    }
                }
            }
            for (int j = 1; j <= N; j++) {
                for (int k = 1; k <= N; k++) {
                    for (int i = 1; i < k; i++) {
                        List<Literal> newSet = new ArrayList<>();
                        newSet.add(new Literal(prefix + Integer.toString(i) + Integer.toString(j
                        ), true));
                        newSet.add(new Literal(prefix + Integer.toString(k) + Integer.toString(j
                        ), true));
                        clauses.add(new Clause(newSet));
                    }
                }
            }
        }
        CNF cnf = new CNF(clauses);
        // Hints
        // 1) "The Brit lives in the red house."
        DNF dnf1 = sameHouse("n1", "h5");
        cnf.addAll(dnf1.toCNF());
        // 2) "The Swede keeps dogs as pets."
        DNF dnf2 = sameHouse("n2", "p1");
        cnf.addAll(dnf2.toCNF());
        // 3) "The Dane drinks tea."
        DNF dnf3 = sameHouse("n3", "b1");
        cnf.addAll(dnf3.toCNF());
        // 4) "The green house is on the left of the white house."
        DNF dnf4 = leftHouse("h1", "h2");
        cnf.addAll(dnf4.toCNF());
        // 5) "The green house’s owner drinks coffee."
        DNF dnf5 = sameHouse("h2", "b2");
        cnf.addAll(dnf5.toCNF());
        // 6) "The person who smokes Pall Mall rears birds."
        DNF dnf6 = sameHouse("c1", "p2");
        cnf.addAll(dnf6.toCNF());
        // 7) "The owner of the yellow house smokes Dunhill."
        DNF dnf7 = sameHouse("h3", "c2");
        cnf.addAll(dnf7.toCNF());
        // 8) "The man living in the center house drinks milk."
        DNF dnf8 = atomicHouse("b33");
        cnf.addAll(dnf8.toCNF());
        // 9) "The Norwegian lives in the first house."
        DNF dnf9 = atomicHouse("n41");
        cnf.addAll(dnf9.toCNF());
        // 10) "The man who smokes Blends lives next to the one who keeps cats."
        DNF dnf10 = livesNextHouse("c3","p3");
        cnf.addAll(dnf10.toCNF());
        // 11) "The man who keeps the horse lives next to the man who smokes Dunhill."
        DNF dnf11 = livesNextHouse("p4","c2");
        cnf.addAll(dnf11.toCNF());
        // 12) "The owner who smokes Bluemasters drinks beer."
        DNF dnf12 = sameHouse("c4", "b4");
        cnf.addAll(dnf12.toCNF());
        // 13) "The German smokes Prince."
        DNF dnf13 = sameHouse("n5", "c5");
        cnf.addAll(dnf13.toCNF());
        // 14) "The Norwegian lives next to the blue house."
        DNF dnf14 = livesNextHouse("n4","h4");
        cnf.addAll(dnf14.toCNF());
        // 15) "The man who smokes Blends has a neighbor who drinks water."
        DNF dnf15 = livesNextHouse("c3","b5");
        cnf.addAll(dnf15.toCNF());
        // Result CNF
        this.cnf = cnf;
    }

    public DNF sameHouse(String symbol1, String symbol2) {
        List<List<Literal>> disjunctions = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            List<Literal> conjunction = new ArrayList<>();
            conjunction.add(new Literal(symbol1 + j, false));
            conjunction.add(new Literal(symbol2 + j, false));
            disjunctions.add(conjunction);
        }
        return new DNF(disjunctions);
    }

    public DNF leftHouse(String symbol1, String symbol2) {
        List<List<Literal>> disjunctions = new ArrayList<>();
        for (int i = 2; i <= 5; i++) {
                List<Literal> conjunction = new ArrayList<>();
                conjunction.add(new Literal(symbol1 + i, false));
                conjunction.add(new Literal(symbol2 + (i-1), false));
                disjunctions.add(conjunction);
        }
        return new DNF(disjunctions);
    }


    public DNF atomicHouse(String symbol) {
        List<List<Literal>> disjunctions = new ArrayList<>();
        List<Literal> conjunction = new ArrayList<>();
        conjunction.add(new Literal(symbol, false));
        disjunctions.add(conjunction);
        return new DNF(disjunctions);
    }

    public DNF livesNextHouse(String symbol1, String symbol2) {
        List<List<Literal>> disjunctions = new ArrayList<>();
        for (int j = 1; j <= 4; j++) {
            List<Literal> conjunction = new ArrayList<>();
            conjunction.add(new Literal(symbol1 + j, false));
            int k = j + 1;
            conjunction.add(new Literal(symbol2 + k, false));
            disjunctions.add(conjunction);
        }
        for (int j = 2; j <= 5; j++) {
            List<Literal> conjunction = new ArrayList<>();
            conjunction.add(new Literal(symbol1 + j, false));
            int k = j - 1;
            conjunction.add(new Literal(symbol2 + k, false));
            disjunctions.add(conjunction);
        }
        return new DNF(disjunctions);
    }

    public CNF getCnf() {
        return cnf;
    }

    public List<Prop> getAP() {
        return AP;
    }

    public static void printTruthAssignement(TruthAssignment tau) {
        HashMap<String, String> printing = new HashMap<>();
        List<String> n = new ArrayList<>(Arrays.asList("Brit", "Swede", "Dane", "Norwegian", "German"));
        List<String> h = new ArrayList<>(Arrays.asList("White", "Green", "Yellow", "Blue", "Red"));
        List<String> b = new ArrayList<>(Arrays.asList("Tea", "Coffee", "Milk", "Beer", "Water"));
        List<String> c = new ArrayList<>(Arrays.asList("Pall Mall", "Dunhill", "Blends", "Bluemasters", "Prince"));
        List<String> p = new ArrayList<>(Arrays.asList("Dogs", "Birds", "Cat", "Horse", "Fish"));
        List<String> d = new ArrayList<>(Arrays.asList("1","2","3","4","5"));

        // Creation of hashmap
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                printing.put("n" + i + j,n.get(i-1) + " is in house " + d.get(j-1));
                printing.put("h" + i + j,h.get(i-1) + " is in house " + d.get(j-1));
                printing.put("b" + i + j,b.get(i-1) + " is in house " + d.get(j-1));
                printing.put("c" + i + j,c.get(i-1) + " is in house " + d.get(j-1));
                printing.put("p" + i + j,p.get(i-1) + " is in house " + d.get(j-1));
            }
        }

        Set<Prop> props = tau.getTau();
        StringBuilder s = new StringBuilder();
        for (Prop pr : props) {
            assert printing.get(pr.toString()) != null;
            s.append(printing.get(pr.toString())).append("\n");
        }
        System.out.println(s);
    }

    public static void main(String[] args) {
        EinsteinPuzzle puzzle = new EinsteinPuzzle();
        CNF cnf = puzzle.getCnf();
        DPLL solver = new DPLL(puzzle.getAP());
        TruthAssignment tau = solver.SAT(cnf);
        System.out.println(cnf);
        System.out.println(tau);
        EinsteinPuzzle.printTruthAssignement(tau);

    }
}
