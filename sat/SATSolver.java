package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
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
        Clause smallest = clauses.first();
        for (Clause c:clauses){
            //first check if valid clause
            if (c.isEmpty())
                return null;
            //if so, obtain size
            if (c.size()< smallest.size())
                smallest = c;
            //stop when size is 1, which is the smallest possible valid size
            if (smallest.isUnit())
                break;
        }
        Literal l = smallest.chooseLiteral();
        Variable v = l.getVariable();
        // if the clause has only 1 literal
        if (smallest.isUnit()) {
            // bind its variable to the environment
            if (l instanceof PosLiteral){
                env = env.putTrue(v);
            }
            else{
                env = env.putFalse(v);
            }
            // substitute, then recursively call solve
            return solve(substitute(clauses, l), env);
        }
        else {
            // otherwise, first try setting the literal to true
            env = env.putTrue(v);
            // then substitute and solve recursively
            Environment newEnv = solve(substitute(clauses, l), env);
            // if that fails,
            if (newEnv == null) {
                // try setting literal to false
                env = env.putFalse(v);
                // and solve recursively
                return solve(substitute(clauses, l.getNegation()), env);
            }
            else{
                return newEnv;}
        }
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
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        //nothing to substitute
        if (clauses.isEmpty())
            return clauses;
        //create new clause
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Clause c:clauses){
            //"reduce" the clause
            Clause clause = c.reduce(l);
            //if clause is still valid, add it to the new clause
            if (clause!=null)
                result=result.add(clause);
        }
        return result;
    }
}
