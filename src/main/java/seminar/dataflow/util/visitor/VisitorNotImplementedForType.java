package seminar.dataflow.util.visitor;

/**
 * Exception thrown when an expression type (a Tai-e {@link pascal.taie.ir.exp.Exp}) is not supported by
 * {@link seminar.dataflow.util.visitor.AbstractValueVisitor}.
 */
public class VisitorNotImplementedForType extends RuntimeException {
    public VisitorNotImplementedForType(String name) {
        super(name);
    }
}
