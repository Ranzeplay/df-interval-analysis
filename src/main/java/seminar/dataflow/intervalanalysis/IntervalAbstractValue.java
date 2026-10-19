package seminar.dataflow.intervalanalysis;

import java.util.ArrayList;

public class IntervalAbstractValue {
    public static final int NEG_INFINITY = Integer.MIN_VALUE;
    public static final int POS_INFINITY = Integer.MAX_VALUE;

    private final int lower;
    private final int upper;
    private final boolean isBottom;
    private final boolean isTop;

    // Bottom element (represents "not an integer")
    private static final IntervalAbstractValue BOTTOM = new IntervalAbstractValue(true, false);
    // Top element (represents the range of all integers)
    private static final IntervalAbstractValue TOP = new IntervalAbstractValue(false, true);

    // Private constructor for bottom and top
    private IntervalAbstractValue(boolean isBottom, boolean isTop) {
        if (isBottom && isTop) {
            throw new IllegalArgumentException("Invalid state both TOP and BOTTOM");
        }
        this.isBottom = isBottom;
        this.lower = isTop ? NEG_INFINITY : 0;
        this.upper = isTop ? POS_INFINITY : 0;
        this.isTop = isTop;
    }


    // region constructors
    public IntervalAbstractValue(int lower, int upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Invalid interval: lower > upper");
        }
        this.lower = lower;
        this.upper = upper;
        this.isBottom = false;
        this.isTop = (lower == NEG_INFINITY && upper == POS_INFINITY);
    }

    public static IntervalAbstractValue bottom() {
        return BOTTOM;
    }

    public static IntervalAbstractValue singleton(int value) {
        return new IntervalAbstractValue(value, value);
    }

    public static IntervalAbstractValue top() {
        return TOP;
    }
    // endregion

    // region abstract operations

    public IntervalAbstractValue add(IntervalAbstractValue other){
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue widenAdd(IntervalAbstractValue other, ArrayList<Integer> breaks) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue mul(IntervalAbstractValue other){
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue widenMul(IntervalAbstractValue other, ArrayList<Integer> breaks) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue widenSubtract(IntervalAbstractValue other, ArrayList<Integer> breaks) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue subtract(IntervalAbstractValue other){
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue div(IntervalAbstractValue other){
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue widenDiv(IntervalAbstractValue other, ArrayList<Integer> breaks) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public IntervalAbstractValue join(IntervalAbstractValue other) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    public boolean isBottom() {
        return isBottom;
    }

    public boolean isTop() {
        return isTop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntervalAbstractValue that = (IntervalAbstractValue) o;
        return this.toString().equals(that.toString());
    }

    // endregion

    @Override
    public String toString() {
        if (isBottom) return "⊥";
        if (lower == NEG_INFINITY && upper == POS_INFINITY) return "[-∞,∞]";
        if (lower == NEG_INFINITY && upper == NEG_INFINITY) return "[-∞,-∞]";
        if (lower == POS_INFINITY && upper == POS_INFINITY) return "[∞,∞]";
        if (lower == NEG_INFINITY) return "[-∞," + upper + "]";
        if (upper == POS_INFINITY) return "[" + lower + ",∞]";
        return "[" + lower + "," + upper + "]";
    }
}