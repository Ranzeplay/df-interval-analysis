package seminar.dataflow.intervalanalysis;

import seminar.dataflow.util.visitor.ValueVisitor;
import pascal.taie.ir.exp.Var;

public class IntervalAbstractValueVisitorNaive extends IntervalAbstractValueVisitor {

    private IntervalAbstractValue resolvedValue;

    public IntervalAbstractValueVisitorNaive(IntervalAbstractState state) {
        super(state);
    }

    @Override
    public IntervalAbstractValueVisitor newInstanceFromState(IntervalAbstractState in) {
        return new IntervalAbstractValueVisitorNaive(in);
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
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
        // resolvedValue = ...;
    }

    @Override
    public IntervalAbstractValue done() {
        return resolvedValue;
    }

    @Override
    public ValueVisitor<IntervalAbstractValue> cloneVisitor() {
        return new IntervalAbstractValueVisitorNaive(state);
    }

}
