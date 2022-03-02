package sat.formula;

public class textonly {

    /* public class SATSolver {

        if (smallest.isUnit()) {
        	Literal l = smallest.chooseLiteral();
        	env = l instanceof PosLiteral ? env.putTrue(l.getVariable()) : env.putFalse(l.getVariable());
        	if l is a PosLiteral,
        	    env.putTrue(l.getVariable)
        	else
        	    env.putFalse(l.getVariable)

            return solve(substitute(clauses,l), env);

        }else {
        	Literal l = smallest.chooseLiteral();
        	// set the literal to TRUE, substitute for it in all the clauses, then solve() recursively

        	ImList<Clause> reducedLiteralsPos = substitute(clauses, l);
        	Environment trueLiteral = solve(reducedLiteralsPos, env.putTrue(l.getVariable()));
        	return trueLiteral != null ? trueLiteral : solve(substitute(clauses,l.getNegation()),env.putFalse(l.getVariable()));
        	// return trueLiteral if trueLiteral is not null, else return solve(redClauseNegLit)
        }

     */
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.

    public static Environment solve(Formula formula) {
        // this calls the next function
        ImList<Clause> clauses = formula.getClauses();
        Environment env = new Environment();
        return solve(clauses, env);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.

    private static Environment solve(ImList<Clause> clauses, Environment env) {
        //If there are no clauses, the formula is trivially satisfiable.
        //• If there is an empty clause, the clause list is unsatisfiable -- fail and backtrack. (use empty
        //	clause to denote a clause evaluated to FALSE based on the variable binding in the
        //	environment)
        if (clauses.isEmpty()){
            return env;
        }
        //empty clause
        for (Clause clause : clauses){
            if (clause.isEmpty()){
                return null;
            }
        }

        //• Otherwise, find the smallest clause (by number of literals).
        Clause smallestClause = new Clause();
        int smallestSize = 99999999;
        for (Clause clause: clauses){
            if (clause.size()<smallestSize){
                smallestSize = clause.size();
                smallestClause = clause;
            }
            if (smallestSize == 1){
                break;
            }
        }

        //	o If the clause has only one literal, bind its variable in the environment so that the
        //	clause is satisfied, substitute for the variable in all the other clauses (using the
        //	suggested substitute() method), and recursively call solve().
        //	o Otherwise, pick an arbitrary literal from this small clause:
        //		First try setting the literal to TRUE, substitute for it in all the clauses, then
        //		solve() recursively.
        //		If that fails, then try setting the literal to FALSE, substitute, and solve()
        //		recursively.

        Environment newEnv = new Environment(); //env to put the var as true
        Literal literal = smallestClause.chooseLiteral();
        String checker = literal.toString();
        Variable var = literal.getVariable();
        Environment output = new Environment(); //output
        ImList<Clause> newClauses = new EmptyImList<Clause>(); //Substituted list

        if (checker.startsWith("~")){
            newEnv = env.putFalse(var);
        }else{
            newEnv = env.putTrue(var);
        }
        newClauses = substitute(clauses, literal);
        if (smallestClause.isUnit()){
            output = solve(newClauses, newEnv);
        }else{
            output = solve(newClauses, newEnv);
            if (output == null){
                if (checker.startsWith("~")){
                    newEnv = env.putTrue(var);
                }else{
                    newEnv = env.putFalse(var);
                }
                Literal nLiteral = literal.getNegation();
                newClauses = substitute(clauses, nLiteral);
                output = solve(newClauses, newEnv);

            }
        }
        return output;

    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true

    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        // Parse through all the clauses in the ImList and change the literal l to be true using reduce.
        // If newClause comes out as not null (means still not true, because if true we can ignore) then add it to the new ImList
        Clause newClause = new Clause();
        ImList<Clause> outClauses = new EmptyImList<Clause>();
        if (clauses.isEmpty()){
            return outClauses;
        }
        for (Clause clause : clauses){
            if (!clause.isEmpty() && clause != null){
                newClause = clause.reduce(l);
            }
            if (newClause != null){
                outClauses = outClauses.add(newClause);
            }
        }
        return outClauses;
    }


    // 2nd version
}*/

    /* public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();




    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String args[]) throws IOException {
        System.out.println("Reading File");
    	long readTime = System.nanoTime();
    	File fin = null;
        Scanner bin = null;
        try {
            fin=new File("Test.cnf"); //import file
            bin=new Scanner(fin);

            String line;
            boolean commentCheck=true;
            while(commentCheck!=false){
                String[] commentRemove=bin.nextLine().split(" ");
                if(commentRemove[0]!="c"||commentRemove[0]!="C"){
                    commentCheck=false;
                }


            }
            String[] format=bin.nextLine().split(" ");
            int NumberOfClauses=Integer.parseInt(format[3]);//get the number of clauses
            Formula f = new Formula(); //create and instance of the formula
            while (f.getSize()!=NumberOfClauses) {

                line=bin.nextLine();
                if(line.length()>0){
                    String[] tempLine=line.split(" ");

                    Clause c = new Clause();

                    for(String i:tempLine){
                        if(Integer.parseInt(i)==0){
                            break;
                        }
                        Literal literal = PosLiteral.make(Integer.toString(Math.abs(Integer.parseInt(i))));//makes literal instance


                        if((Integer.parseInt(i))<0){ //add the negated Integer to the clause if string is negative
                            c=c.add(literal.getNegation());
                        }
                        else if ((Integer.parseInt(i))>0){ //add the postitive Integer to the clause if string is positive
                            c=c.add(literal);
                        }
                        if (c == null){
                        	c = new Clause();
                        }
                    }
                    f=f.addClause(c); //add the clauses to the formula
                }
            }
            String fileName = "D:/School/ISTD/Java/2D/BoolAssignment.txt";
            PrintWriter write = new PrintWriter(fileName, "UTF-8");
            long endReadTime = System.nanoTime();
            long tReadTime = endReadTime - readTime;
            System.out.println("Reading Time: " + tReadTime/1000000000.0 + "s");
            System.out.println("SAT Solver starts");
            long started = System.nanoTime();
            Environment env = SATSolver.solve(f);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Solving Time: " + timeTaken/1000000.0 + "ms");
            System.out.println("Total Time: " + (timeTaken+tReadTime)/1000000000.0 + "s");
            if (env == null){
            	System.out.println("Formula Unsatisfiable");
            }else{
            	System.out.println("Formula Satisfiable");
            	String bindings = env.toString();
            	System.out.println(bindings);
            	bindings = bindings.substring(bindings.indexOf("[")+1, bindings.indexOf("]"));
            	String[] bindingNew = bindings.split(", ");
            	for (String binding : bindingNew){
            		String[] bind = binding.split("->");
            		write.println(bind[0] + ":" + bind[1]);
            	}


            }
            write.close();
            //TO BE REMOVED!!!!! WILL SLOW DOWN THE CODE===========================================
//            if(NumberOfClauses==f.getSize()){
//                System.out.println("Fomula generated Successfully!");
//            }
//            else{
//                System.out.println("ExpectedNumberOfClauses: "+ NumberOfClauses);
//                System.out.println("FormulaSize: "+ f.getSize());
//            }
//            System.out.println(f.toString());
            //====================================================================================

        }finally {
            if (bin != null) {
                bin.close();

            }
        }
    }
    public void testSATSolver1(){
        // For example , (a v b):
        Environment e = SATSolver.solve(makeFm(makeCl(a,b)));
        /*
        assertTrue( "one of the literals should be set to true",
        Bool.TRUE == e.get(a.getVariable())
        || Bool.TRUE == e.get(b.getVariable())	);


    }


public void testSATSolver2(){
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
        /*
        assertEquals( Bool.FALSE, e.get(na.getVariable()));

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
}

public class SATSolverTest {

    public static String name = "largeUnsat.cnf";
    public static String output = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\java output files\\"
            + "BoolAssignment.txt";
    public static String file_name = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\project-2d-starting\\sampleCNF\\"
            + name;


    // ... it allow users to insert many many arguments of e
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

    public static void main(String[] args) {
        // Initialize fields 	*******************************************************************
        Boolean cnfReadings = false;

        ArrayList<Clause> clauses = new ArrayList<>();

        ArrayList<Literal> clause = new ArrayList<>();
        String line;
        String[] info;
        Literal[] l;
        // Read file			*******************************************************************
        BufferedReader br;

        System.out.println("[INFO] Preparing the required containers...");
        long startread = System.nanoTime();		// how fast am i taking to read the files?
        try {
            File file = new File(file_name);
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {							// stop reading when it reach the end of file
                if (cnfReadings) {												// only start parsing when info line is seen (see SATformat.pdf)
                    info = line.split(" ");
                    for (String x: info) {
                        if (info.length > 1) {
                            int currentLiteral = Integer.parseInt(x);
                            if (currentLiteral > 0) {
                                clause.add(PosLiteral.make(x));	// this literal will have the class PosLiteral
                            } else if (currentLiteral < 0){
                                clause.add(NegLiteral.make(x));	// this literal will have the class NegLiteral
                            }else {
                                l = new Literal[clause.size()];
                                clause.toArray(l);
                                Clause balapoopi = makeCl(l);							// wooo make our Clause!!!!
                                if (balapoopi!=null) {
                                    clauses.add(balapoopi);
                                }clause.clear();											// reset the clause ArrayList to empty ArrayList
                            }
                        }


                    }
                }
                else if (line.charAt(0) == 'p') {
                    cnfReadings = true;
                }
            }
            br.close();
            cnfReadings=false;
            long endread = System.nanoTime();
            long elapsedread = endread - startread;
            System.out.println("[INFO] Time Taken to Read File: " + elapsedread/1000000000.0 + "s");



            // Solve the CNF question	*******************************************************************
            Formula solver = null;
            Clause[] justAnotherClauseArray = new Clause[clauses.size()];
            clauses.toArray(justAnotherClauseArray);
            solver = makeFm(justAnotherClauseArray);
            System.out.println("SAT solver start!!!");
            long start = System.nanoTime();
            Environment e = SATSolver.solve(solver);
            long end = System.nanoTime();
            long elapsed = end - start;
            System.out.println("[INFO] Time Taken: " + elapsed/1000000.0 + "ms");

            // Output results into BoolAssignment.txt	*******************************************************************


            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");

            if (e!=null) {
                String woooFinallyDoneWithLife = (e.toString()).substring(13, e.toString().length()-1);
                String[] woooFinallyDoneWithSAT = woooFinallyDoneWithLife.split(", ");
                for (String i: woooFinallyDoneWithSAT) {
                    String[] woooFinallyDoneWith2D = i.split("->");
                    bw.append(woooFinallyDoneWith2D[0] + ":" + woooFinallyDoneWith2D[1]);
                    bw.newLine();
                }


                bw.close();
                System.out.println("[SYSTEM] Yes, it is Satisfiable! :D");
            }
            else {
                System.out.println("[SYSTEM] oh no, it is not satisfiable.... :(");
            }
        }
        catch (IOException err) {
            err.printStackTrace();
        }

    }
}*/