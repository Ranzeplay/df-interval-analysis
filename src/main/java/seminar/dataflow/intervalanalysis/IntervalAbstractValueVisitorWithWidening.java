package seminar.dataflow.intervalanalysis;

import seminar.dataflow.util.visitor.ValueVisitor;
import pascal.taie.ir.exp.Var;

import java.util.ArrayList;

public class IntervalAbstractValueVisitorWithWidening extends IntervalAbstractValueVisitor {

    private final ArrayList<Integer> breaks;

    private IntervalAbstractValue resolvedValue;

    public IntervalAbstractValueVisitorWithWidening(IntervalAbstractState state, ArrayList<Integer> breaks) {
        super(state);
        if (breaks.isEmpty()) throw new IllegalArgumentException("Breaks should be non empty.");
        boolean containsInfinity = breaks.contains(IntervalAbstractValue.NEG_INFINITY) && breaks.contains(IntervalAbstractValue.POS_INFINITY);
        if (!containsInfinity) throw new IllegalArgumentException("Breaks should contain infinity.");
        this.breaks = breaks;
    }

    @Override
    protected void visitLocal(Var variable) {
        String varName = variable.getName();
        resolvedValue = state.getIntervalForVariable(varName);
    }

    @Override
    protected void visitDivExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    protected void visitMulExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    protected void visitSubExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    protected void visitAddExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    protected void visitIntegerConstant(int value) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    protected void visitParameter(Var parameter) {
        resolvedValue = IntervalAbstractValue.top();
    }

    @Override
    public IntervalAbstractValue done() {
        return resolvedValue;
    }

    @Override
    public ValueVisitor<IntervalAbstractValue> cloneVisitor() {
        return new IntervalAbstractValueVisitorWithWidening(state, breaks);
    }

    @Override
    public IntervalAbstractValueVisitor newInstanceFromState(IntervalAbstractState in) {
        return new IntervalAbstractValueVisitorWithWidening(in, breaks);
    }
}
