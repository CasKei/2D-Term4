package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment env = new Environment();
        return solve(clauses, env);
        //throw new RuntimeException("not yet implemented.");
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
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty())
            return env;

        // 1 loop to check a) all clauses valid and b) for the smallest clause by no. of literals
        Clause smallest = clauses.first();
        int num = smallest.size();
        for (Clause c:clauses.rest()){
            if (c.size()<num)
                num = c.size();
                smallest = c;
            if (num == 0)
                return null;
        }

        Literal l = smallest.chooseLiteral();
        Variable v = l.getVariable();

        // if the clause has only 1 literal
        if (smallest.isUnit()) {
            // bind its variable to the environment
            if (l instanceof PosLiteral)
                env = env.putTrue(v);
            else
                env = env.putFalse(v);
            // substitute, then recursively call solve
            return solve(substitute(clauses, l), env);
        }else
            // otherwise, first try setting the literal to true
            env = env.putTrue(v);
            // then substitute and solve recursively
            Environment newEnv = solve(substitute(clauses,l), env);
            // if that fails,
            if (newEnv == null) {
                // try setting literal to false
                env = env.putFalse(v);
                l = l.getNegation();
                // and solve recursively
                return solve(substitute(clauses, l), env);
            }else
                return newEnv;
    }
        //throw new RuntimeException("not yet implemented.");

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Clause c:clauses){
            Clause nclause = c.reduce(l);
            if (nclause != null)
                result = result.add(nclause);
        }
        return result;

    }

}