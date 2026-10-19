package seminar.dataflow.intervalanalysis;

import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.DefinitionStmt;
import pascal.taie.ir.stmt.Stmt;

import java.util.HashMap;
import java.util.Map;

/**
 * Narrowing pass: starting from the (widened) result of {@link IntervalAnalysis}, it recomputes
 * the states with the naive (non-widening) operations until they stop changing, which refines
 * intervals that widening over-approximated.
 */
public class NarrowingAnalysis {
    private final CFG<Stmt> cfg;
    private final Map<Stmt, IntervalAbstractState> outSets;
    private final Map<Stmt, IntervalAbstractState> inSets;

    public NarrowingAnalysis(CFG<Stmt> cfg, Map<Stmt, IntervalAbstractState> outSets) {
        this.cfg = cfg;
        this.outSets = new HashMap<>(outSets);
        this.inSets = new HashMap<>();
    }

    public void startAnalysis() {
        boolean changed = true;
        int maxIterations = 100;
        int iteration = 0;

        while (changed && iteration < maxIterations) {
            changed = false;
            iteration++;

            for (Stmt stmt : cfg.getNodes()) {
                // The entry state holds the parameters' values: it stays as computed.
                if (cfg.isEntry(stmt)) {
                    continue;
                }
                IntervalAbstractState previousOut = outSets.get(stmt).cloneState();

                IntervalAbstractState inSet = calculateInSet(stmt);
                inSets.put(stmt, inSet);

                IntervalAbstractState newOutSet = computeNewOutSet(stmt, inSet);

                if (!newOutSet.equals(previousOut)) {
                    changed = true;
                    outSets.put(stmt, newOutSet);
                }
            }
        }

        System.out.println("Narrowing analysis converged after " + iteration + " iterations");
    }

    private IntervalAbstractState calculateInSet(Stmt stmt) {
        IntervalAbstractState result = new IntervalAbstractState();
        boolean first = true;

        for (Stmt predecessor : cfg.getPredsOf(stmt)) {
            IntervalAbstractState predOut = outSets.get(predecessor);
            if (predOut != null) {
                if (first) {
                    result.copy(predOut);
                    first = false;
                } else {
                    result = result.join(predOut);
                }
            }
        }

        return result;
    }

    private IntervalAbstractState computeNewOutSet(Stmt stmt, IntervalAbstractState inSet) {
        IntervalAbstractState outSet = inSet.cloneState();

        if (stmt instanceof DefinitionStmt && ((DefinitionStmt<?, ?>) stmt).getLValue() instanceof Var) {
            DefinitionStmt<?, ?> definition = (DefinitionStmt<?, ?>) stmt;
            Var variable = (Var) definition.getLValue();

            IntervalAbstractValueVisitorNaive naiveVisitor = new IntervalAbstractValueVisitorNaive(inSet);
            IntervalAbstractValue resolvedValue = naiveVisitor.visit(definition.getRValue()).done();

            outSet = outSet.setIntervalForVariable(variable.getName(), resolvedValue);
        }

        return outSet;
    }

    public Map<Stmt, IntervalAbstractState> getOutSets() {
        return outSets;
    }

    public Map<Stmt, IntervalAbstractState> getInSets() {
        return inSets;
    }
}
