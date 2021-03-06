package FormulaGenerator;

import Solver.CNF;
import Solver.Clause;
import Solver.Literal;
import Solver.Prop;

import java.io.*;
import java.util.*;

public class RandomGenerator {
    int N;              // Nb of variables
    int K = 3;          // Nb of distinct literals per clause
    int L;              // Nb of clauses
    List<Prop> props;    // Set of variables
    List<Literal> literals; // Set of literals
    Random rand;

    public RandomGenerator(int n, int l) {
        N = n;
        L = l;
        List<Prop> props = new ArrayList<>();
        List<Literal> literals = new ArrayList<>();
        // Generation of props
        for (int i = 1; i <= n; i++) {
            // Name of the vars: x_i
            Prop p = new Prop("x" + i);
            props.add(p);
            literals.add(new Literal(p, false));
            literals.add(new Literal(p, true));
        }
        this.props = props;
        this.literals = literals;
        rand = new Random();
        assert N == this.props.size();
    }

    public RandomGenerator(int n, int l, int seed) {
        N = n;
        L = l;
        List<Prop> props = new ArrayList<>();
        List<Literal> literals = new ArrayList<>();
        // Generation of props
        for (int i = 1; i <= n; i++) {
            // Name of the vars: x_i
            Prop p = new Prop("x" + i);
            props.add(p);
            literals.add(new Literal(p, false));
            literals.add(new Literal(p, true));
        }
        this.props = props;
        this.literals = literals;
        rand = new Random(seed);
        assert N == this.props.size();
    }


    public List<Prop> getProps() {
        return props;
    }

    /**
     * 3-SAT generator function
     * @return a randomly-generated 3-SAT formula
     */
    public CNF generate3SAT() {
        // Clean up the props
        for (Prop p : props) {
            p.reset();
        }
        // Check if we have enough variables
        assert N > 2;
        // Clauses construction
        List<Clause> clauses = new ArrayList<>();
        for (int i = 0; i < L; i++) {
            Collections.shuffle(literals, rand);
            clauses.add(new Clause(new ArrayList<>(Arrays.asList(literals.get(0), literals.get(1), literals.get(2)))));
        }
        return new CNF(clauses);
    }


//    /**
//     * 3-SAT generator function
//     * @return a randomly-generated 3-SAT formula
//     */
//    public CNF generate3SAT() {
//        // Clean up the props
//        for (Prop p : props) {
//            p.reset();
//        }
//        // Check if we have enough variables
//        assert N > 2;
//        // Clauses construction
//        List<Clause> clauses = new ArrayList<>();
//        for (int i = 0; i < L; i++) {
//            // Consider Prop as a circular list
//            int k = rand.nextInt(N-1);  // Random integer between 0 and N-1: index in props
//            Literal x, y, z;
//            if (k < N-2) {
//                x = new Literal(props.get(k), rand.nextBoolean());
//                y = new Literal(props.get(k+1), rand.nextBoolean());
//                z = new Literal(props.get(k+2), rand.nextBoolean());
//            } else {
//                if (k == N-2) {
//                    x = new Literal(props.get(k), rand.nextBoolean());
//                    y = new Literal(props.get(k+1), rand.nextBoolean());
//                    z = new Literal(props.get(0), rand.nextBoolean());
//                } else  {
//                    x = new Literal(props.get(k), rand.nextBoolean());
//                    y = new Literal(props.get(0), rand.nextBoolean());
//                    z = new Literal(props.get(1), rand.nextBoolean());
//                }
//            }
//            assert x != y && x != z && y != z;
//            clauses.add(new Clause(new ArrayList<>(Arrays.asList(x,y,z))));
//        }
//        return new CNF(clauses);
//    }

}
