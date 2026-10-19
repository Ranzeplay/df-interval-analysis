package seminar.dataflow.intervalanalysis;

import pascal.taie.analysis.dataflow.analysis.AbstractDataflowAnalysis;
import pascal.taie.analysis.dataflow.fact.DataflowResult;
import pascal.taie.analysis.dataflow.solver.Solver;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.DefinitionStmt;
import pascal.taie.ir.stmt.Stmt;

import java.util.HashMap;
import java.util.Map;

/**
 * Forward dataflow analysis that computes an interval for each integer variable.
 * The visitor decides how values are computed (naive, or with widening); optionally, a
 * narrowing pass refines the result afterwards.
 */
public class IntervalAnalysis extends AbstractDataflowAnalysis<Stmt, IntervalAbstractState> {
    private final IntervalAbstractValueVisitor visitor;

    /**
     * The OUT state of each node of the control-flow graph (including its entry and exit nodes).
     */
    public Map<Stmt, IntervalAbstractState> outSet = new HashMap<>();

    public IntervalAnalysis(CFG<Stmt> cfg, IntervalAbstractValueVisitor visitor, boolean applyNarrowing) {
        super(cfg);
        this.visitor = visitor;
        DataflowResult<Stmt, IntervalAbstractState> result = Solver.<Stmt, IntervalAbstractState>getSolver().solve(this);
        for (Stmt node : cfg.getNodes()) {
            outSet.put(node, result.getOutFact(node));
        }
        if (applyNarrowing) {
            NarrowingAnalysis narrowingAnalysis = new NarrowingAnalysis(cfg, outSet);
            narrowingAnalysis.startAnalysis();
            outSet = narrowingAnalysis.getOutSets();
        }
    }

    @Override
    public boolean isForward() {
        return true;
    }

    /**
     * The state at the entry of the method: the interval each parameter holds when the method starts.
     */
    @Override
    public IntervalAbstractState newBoundaryFact() {
        IntervalAbstractState state = new IntervalAbstractState();
        for (Var parameter : cfg.getIR().getParams()) {
            IntervalAbstractValue value = visitor.newInstanceFromState(state).visitParameterDefinition(parameter).done();
            state = state.setIntervalForVariable(parameter.getName(), value);
        }
        return state;
    }

    @Override
    public IntervalAbstractState newInitialFact() {
        return new IntervalAbstractState();
    }

    @Override
    public boolean transferNode(Stmt stmt, IntervalAbstractState in, IntervalAbstractState out) {
        IntervalAbstractState newOut = in.cloneState();
        // Check if the statement assigns a value to a variable.
        if (stmt instanceof DefinitionStmt && ((DefinitionStmt<?, ?>) stmt).getLValue() instanceof Var) {
            DefinitionStmt<?, ?> definition = (DefinitionStmt<?, ?>) stmt;
            Var variable = (Var) definition.getLValue();

            // Create a visitor for the right-hand side of the assignment.
            IntervalAbstractValue resolvedValue = visitor.newInstanceFromState(in).visit(definition.getRValue()).done();

            // Set the IntervalAbstractValue in the OUT state for the variable being assigned.
            newOut = newOut.setIntervalForVariable(variable.getName(), resolvedValue);
        }

        boolean changed = !newOut.equals(out);
        out.clear();
        out.copy(newOut);
        return changed;
    }

    @Override
    public void meetInto(IntervalAbstractState fact, IntervalAbstractState target) {
        IntervalAbstractState joined = target.join(fact);
        target.clear();
        target.copy(joined);
    }
}
