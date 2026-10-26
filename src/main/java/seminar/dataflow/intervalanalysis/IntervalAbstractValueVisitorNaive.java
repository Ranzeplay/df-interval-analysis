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
        resolvedValue = leftOperand.div(rightOperand);
    }

    @Override
    protected void visitMulExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        resolvedValue = leftOperand.mul(rightOperand);
    }

    @Override
    protected void visitSubExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        resolvedValue = leftOperand.subtract(rightOperand);
    }

    @Override
    protected void visitAddExpression(IntervalAbstractValue leftOperand, IntervalAbstractValue rightOperand) {
        resolvedValue = leftOperand.add(rightOperand);
    }

    @Override
    protected void visitIntegerConstant(int value) {
        resolvedValue = IntervalAbstractValue.singleton(value);
    }

    @Override
    protected void visitParameter(Var parameter) {
        visitLocal(parameter);
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
