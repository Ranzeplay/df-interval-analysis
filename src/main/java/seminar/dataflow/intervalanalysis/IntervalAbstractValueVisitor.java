package seminar.dataflow.intervalanalysis;

import seminar.dataflow.util.visitor.AbstractValueVisitor;

public abstract class IntervalAbstractValueVisitor extends AbstractValueVisitor<IntervalAbstractValue> {
    protected final IntervalAbstractState state;

    protected IntervalAbstractValueVisitor(IntervalAbstractState state) {
        this.state = state;
    }

    public abstract IntervalAbstractValueVisitor newInstanceFromState(IntervalAbstractState in);
}
