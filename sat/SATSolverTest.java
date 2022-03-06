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

    //VM option: -Xss128m
    public static void main(String[] args) {
        Formula formula = new Formula();
        try {
            File cnffile = new File(args[0]);
            Scanner filereader = new Scanner(cnffile);
            
            while (filereader.hasNextLine()) {
                String[] lineclause = filereader.nextLine().split(" ");
                //skip past comments and problem line
                if (lineclause[0].equals("c") || lineclause[0].equals("p") || lineclause[0].isEmpty()) {
                    continue;
                }
                Clause clause = new Clause();
                for (String s:lineclause){
                    //0 indicates end of line
                    if (s.equals("0")) {
                        continue;
                    }
                    //creating negative literal vs positive literal
                    if (s.charAt(0)=='-')
                        clause = clause.add(NegLiteral.make(s.substring(1)));
                    else
                        clause = clause.add(PosLiteral.make(s));
                    //fix null pointer exception
                    if (clause==null)
                        clause=new Clause();
                }
                formula = formula.addClause(clause);
            }
            filereader.close();
            //System.out.println(formula);
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
            try{
                System.out.println("Satisfiable");
                File output = new File("BoolAssignment.txt");
                FileWriter boolassignment = new FileWriter(output);
                String temp = e.toString();
                //splitting along ", " will cause first entry to have "Environment:[" attached to the front
                //last entry will also have a "]" attached to the end
                temp = temp.substring(13,temp.length()-1);
                String[] envstring = temp.split(", ");
                for (String s:envstring){
                    String[] pair = s.split("->");
                    boolassignment.write(pair[0]+":"+pair[1]+"\n");
                }
                boolassignment.close();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
