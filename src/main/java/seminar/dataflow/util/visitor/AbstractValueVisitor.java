package seminar.dataflow.util.visitor;

import pascal.taie.ir.exp.ArithmeticExp;
import pascal.taie.ir.exp.Exp;
import pascal.taie.ir.exp.IntLiteral;
import pascal.taie.ir.exp.Var;

/**
 * Base visitor that dispatches on the kind of expression. Tai-e's IR is in 3-address form,
 * so the right-hand side of an assignment is one of:
 * <ul>
 *     <li>an integer constant: {@code x = 0}</li>
 *     <li>a variable: {@code x = y}</li>
 *     <li>an arithmetic operation between two variables: {@code x = y + z}</li>
 * </ul>
 * Constants are never operands: {@code x = y + 1} becomes {@code %ci1 = 1; x = y + %ci1}.
 * @param <T> The result type.
 */
public abstract class AbstractValueVisitor<T> implements ValueVisitor<T> {

    @Override
    public final ValueVisitor<T> visit(Exp value) {
        if (value instanceof IntLiteral) {
            visitIntegerConstant(((IntLiteral) value).getValue());
        } else if (value instanceof ArithmeticExp) {
            doVisitArithmeticExpression((ArithmeticExp) value);
        } else if (value instanceof Var) {
            visitLocal((Var) value);
        } else {
            throw new VisitorNotImplementedForType(value.getClass().getName());
        }
        return this;
    }

    @Override
    public final ValueVisitor<T> visitParameterDefinition(Var parameter) {
        visitParameter(parameter);
        return this;
    }

    public final void doVisitArithmeticExpression(ArithmeticExp value) {
        // Visit the left and right operands (always variables) of the arithmetic expression.
        T leftVisitorResult = cloneVisitor().visit(value.getOperand1()).done();
        T rightVisitorResult = cloneVisitor().visit(value.getOperand2()).done();

        switch (value.getOperator()) {
            case ADD:
                visitAddExpression(leftVisitorResult, rightVisitorResult);
                break;
            case SUB:
                visitSubExpression(leftVisitorResult, rightVisitorResult);
                break;
            case MUL:
                visitMulExpression(leftVisitorResult, rightVisitorResult);
                break;
            case DIV:
                visitDivExpression(leftVisitorResult, rightVisitorResult);
                break;
            default:
                throw new VisitorNotImplementedForType(value.getOperator().toString());
        }
    }

    protected abstract void visitLocal(Var variable);

    protected abstract void visitDivExpression(T leftOperand, T rightOperand);

    protected abstract void visitMulExpression(T leftOperand, T rightOperand);

    protected abstract void visitSubExpression(T leftOperand, T rightOperand);

    protected abstract void visitAddExpression(T leftOperand, T rightOperand);

    protected abstract void visitIntegerConstant(int value);

    /**
     * Called once per method parameter, to compute the value it holds when the method starts.
     * @param parameter the parameter.
     */
    protected abstract void visitParameter(Var parameter);
}
