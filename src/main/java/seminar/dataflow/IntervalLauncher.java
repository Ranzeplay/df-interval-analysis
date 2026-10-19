package seminar.dataflow;

import seminar.dataflow.intervalanalysis.*;
import seminar.dataflow.util.taie.TaieRunner;
import pascal.taie.ir.IR;
import pascal.taie.ir.stmt.Stmt;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Launcher class for the interval analysis.
 */
public class IntervalLauncher {

    private static IntervalAnalysis intervalAnalysis;

    public enum IntervalAnalysisType {
        NAIVE, WIDENING, NARROWING
    }

    /**
     * Runs the analysis on a class and prints the OUT state of each statement.
     * Used by `./gradlew intervalAnalysis -PtargetClass=... -Pmode=...`.
     * @param args the fully qualified name of the class to analyze, and optionally the analysis type.
     */
    public static void main(String[] args) {
        if (args.length == 0 || args[0].isEmpty()) {
            System.out.println("WARNING: No targetClass specified. Please specify a targetClass using the -PtargetClass "
                    + "argument. For example: -PtargetClass=seminar.dataflow.intervaltargets.IntervalAnalysis1");
            return;
        }
        IntervalAnalysisType type = args.length > 1 ? IntervalAnalysisType.valueOf(args[1]) : IntervalAnalysisType.WIDENING;
        System.out.println("Running IntervalAnalysis (" + type + ") on class " + args[0]);
        for (IR ir : TaieRunner.loadMethods(args[0])) {
            IntervalAnalysis analysis = analyzeMethod(ir, type);
            System.out.println();
            System.out.println(ir.getMethod().getSignature());
            for (Stmt stmt : ir) {
                System.out.printf("  line %-3s %-30s OUT: %s%n", stmt.getLineNumber(), stmt, analysis.outSet.get(stmt));
            }
        }
    }

    /**
     * Runs the interval analysis on every method of a given target class.
     * The analysis of the last method is available through {@link #getCurrentIntervalAnalysis()}.
     *
     * @param targetClass the fully qualified name of the class to analyze.
     * @param analysisType the variant of the analysis to run.
     */
    public static void analyzeClass(String targetClass, IntervalAnalysisType analysisType) {
        for (IR ir : TaieRunner.loadMethods(targetClass)) {
            analyzeMethod(ir, analysisType);
        }
    }

    private static IntervalAnalysis analyzeMethod(IR ir, IntervalAnalysisType analysisType) {
        ArrayList<Integer> breaks = new ArrayList<>(
                Arrays.asList(
                        IntervalAbstractValue.NEG_INFINITY, 0, 1, 3, 7, IntervalAbstractValue.POS_INFINITY
                )
        );
        switch (analysisType) {
            case NAIVE:
                intervalAnalysis = new IntervalAnalysis(TaieRunner.buildCFG(ir), new IntervalAbstractValueVisitorNaive(new IntervalAbstractState()), false);
                break;
            case WIDENING:
                intervalAnalysis = new IntervalAnalysis(TaieRunner.buildCFG(ir), new IntervalAbstractValueVisitorWithWidening(new IntervalAbstractState(), breaks), false);
                break;
            case NARROWING:
                intervalAnalysis = new IntervalAnalysis(TaieRunner.buildCFG(ir), new IntervalAbstractValueVisitorWithWidening(new IntervalAbstractState(), breaks), true);
                break;
        }
        return intervalAnalysis;
    }

    public static IntervalAnalysis getCurrentIntervalAnalysis() {
        return intervalAnalysis;
    }
}
