package Parser;

import Solver.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Parser {


    public static Pair<CNF, List<Prop>> parse(String path) throws IOException {
        // Lists to create the CNF
        HashMap<String, Prop> props = new HashMap<>();
        List<Clause> clauses = new ArrayList<>();
        List<Literal> literals = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);
        int n = 0, l = 0;
        boolean found = false;
        for (String line : lines) {
            if (line.charAt(0) == 'p') {
                String[] arr = line.split("[ \n\t]");
                if (arr.length < 4 && !arr[1].equals("cnf")) {
                    throw new IOException("Wrong Problem line formatting!");
                }
                n = Integer.parseInt(arr[2]);
                l = Integer.parseInt(arr[3]);
                found = true;
            }
            else if (found) {
                String[] arr = line.split("[ \n\t]");
                for (String s : arr) {
                    // new Clause
                    if (s.charAt(0) == '0' && literals.size() != 0) {
                        clauses.add(new Clause(literals));
                        literals = new ArrayList<>();
                    } else if (s.charAt(0) == '-') {
                        Prop p = new Prop(s.substring(1));
                        if (!props.containsKey(s.substring(1))) {
                            props.put(s.substring(1), p);
                        } else {
                            p = props.get(s.substring(1));
                        }
                        literals.add(new Literal(p, true));
                    } else {
                        Prop p = new Prop(s);
                        if (!props.containsKey(s)) {
                            props.put(s, p);
                        } else {
                            p = props.get(s);
                        }
                        literals.add(new Literal(p, false));
                    }
                }
            }
        }
        clauses.add(new Clause(literals));
        assert clauses.size() == l && props.size() == n;
        return new Pair<>(new CNF(clauses), new ArrayList<>(props.values()));
    }
}
