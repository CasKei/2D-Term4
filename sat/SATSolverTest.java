package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import static java.lang.Integer.parseInt;

import immutable.ImMap;
import sat.env.*;
import sat.formula.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String[] args) {
        Formula formula = new Formula();
        try {
            File cnffile = new File(args[0]);
            Scanner filereader = new Scanner(cnffile);

            //skip past comment lines and get straight to getting number of clauses
            while (filereader.hasNextLine()) {
                String[] lineclause = filereader.nextLine().split(" ");
                if (lineclause[0].equals("c") || lineclause[0].equals("p") || lineclause[0].isEmpty()) {
                    continue;
                }
                Clause clause = new Clause();
                for (String s:lineclause){
                    if (s.equals("0")) {
                        break;
                    }
                    String[] temp = s.split("-");
                    Literal l = PosLiteral.make(s);
                    if (!temp[0].equals(s))
                        l = l.getNegation();
                    clause = clause.add(l);
                }
                formula = formula.addClause(clause);
            }
            filereader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(formula);
        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        String h = null;

        if (e==null)
            System.out.println("Not Satisfiable");
        else
            h = e.toString();
            System.out.println(h);
            System.out.println("Satisfiable");
    }

    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/
    }


    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

}
