package seminar.dataflow.util.visitor;

import pascal.taie.ir.exp.Exp;
import pascal.taie.ir.exp.Var;

/**
 * Interface for a visitor that visits a Tai-e expression ({@link Exp}) in order to produce an aggregated result.
 * @param <T> The result type.
 */
public interface ValueVisitor<T> {
    /**
     * Visits the given expression (the right-hand side of an assignment) to produce a result.
     * @param value the {@link Exp} to visit.
     * @return this visitor.
     */
    ValueVisitor<T> visit(Exp value);

    /**
     * Computes the value a method parameter holds when the method starts.
     * @param parameter the parameter.
     * @return this visitor.
     */
    ValueVisitor<T> visitParameterDefinition(Var parameter);

    /**
     * Gets the result of this visitor. MUST be called after {@link ValueVisitor#visit(Exp)}
     * or {@link ValueVisitor#visitParameterDefinition(Var)}.
     * @return the result produced by this visitor.
     */
    T done();

    /**
     * User-defined clone method, to have a common interface for when a new {@link ValueVisitor} has to be launched against an
     * internal value, for example an operand of a binary expression. Usually used when the visitor needs some collaborators
     * for working.
     * @return a new visitor
     */
    ValueVisitor<T> cloneVisitor();
}
