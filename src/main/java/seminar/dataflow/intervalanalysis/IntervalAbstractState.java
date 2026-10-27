package seminar.dataflow.intervalanalysis;

import java.util.*;

public class IntervalAbstractState {
    private final Map<String, IntervalAbstractValue> variables;

    // region constructors

    public IntervalAbstractState() {
        this.variables = new HashMap<>();
    }

    public IntervalAbstractState(Map<String, IntervalAbstractValue> variables) {
        this.variables = new HashMap<>(variables);
    }

    // endregion

    // region operations

    public IntervalAbstractValue getIntervalForVariable(String variable) {
        return variables.getOrDefault(variable, IntervalAbstractValue.bottom());
    }

    public IntervalAbstractState setIntervalForVariable(String variable, IntervalAbstractValue interval) {
        Map<String, IntervalAbstractValue> newVars = new HashMap<>(variables);
        newVars.put(variable, interval);
        return new IntervalAbstractState(newVars);
    }

    public IntervalAbstractState join(IntervalAbstractState other) {
        Map<String, IntervalAbstractValue> newVars = new HashMap<>();
        Set<String> allVars = new HashSet<>(this.variables.keySet());
        allVars.addAll(other.variables.keySet());
        for (String var: allVars) {
            IntervalAbstractValue thisInterval = getIntervalForVariable(var);
            IntervalAbstractValue otherInterval = other.getIntervalForVariable(var);
            newVars.put(var, thisInterval.join(otherInterval));
        }
        return new IntervalAbstractState(newVars);
    }

    // endregion

    // region analysis operations


    public Map<String, IntervalAbstractValue> getVariables() {
        return variables;
    }

    public void clear() {
        variables.clear();
    }

    public void copy(IntervalAbstractState another) {
        variables.putAll(another.variables);
    }

    public IntervalAbstractState cloneState() {
        Map<String, IntervalAbstractValue> clonedVars = new HashMap<>(variables);
        return new IntervalAbstractState(clonedVars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntervalAbstractState)) return false;
        IntervalAbstractState that = (IntervalAbstractState) o;
        return Objects.equals(this.variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.variables);
    }

    // endregion

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, IntervalAbstractValue> entry : variables.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(entry.getKey()).append(" → ").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

}
