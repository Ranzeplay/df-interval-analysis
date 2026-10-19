package dataflow.intervalanalysis;

import seminar.dataflow.IntervalLauncher;
import seminar.dataflow.intervalanalysis.IntervalAbstractState;
import seminar.dataflow.intervalanalysis.IntervalAnalysis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pascal.taie.ir.stmt.Return;
import pascal.taie.ir.stmt.Stmt;

import java.util.Set;

public class IntervalTest {

    // Assumes unique return.
    Return findReturnStmt(Set<Stmt> stmts) {
        return (Return) stmts.stream().filter(stmt -> stmt instanceof Return).findFirst().get();
    }

    // Name of the variable that holds the returned value. Tai-e's IR splits a variable that is
    // reassigned with independent values (IntervalAnalysis3's x becomes "x" and "x#1"), so the
    // returned value is the reliable way to get "x at the end of the method".
    String returnedVariable(Return returnStmt) {
        return returnStmt.getValue().getName();
    }

    @Test
    public void testSingletonsAndTopAreProperlyAssigned() {
        IntervalLauncher.analyzeClass("seminar.dataflow.intervaltargets.IntervalAnalysis1", IntervalLauncher.IntervalAnalysisType.NAIVE);
        IntervalAnalysis intervalAnalysis = IntervalLauncher.getCurrentIntervalAnalysis();
        Return returnStmt = findReturnStmt(intervalAnalysis.outSet.keySet());
        IntervalAbstractState stateForReturn = intervalAnalysis.outSet.get(returnStmt);
        Assertions.assertEquals("[5,5]", stateForReturn.getIntervalForVariable("x").toString());
        Assertions.assertEquals("[-∞,∞]", stateForReturn.getIntervalForVariable("n").toString());
        Assertions.assertEquals("[-∞,∞]", stateForReturn.getIntervalForVariable("m").toString());
    }

    @Test
    public void testAdditionsAndSubtractionAreProperlyAssigned() {
        IntervalLauncher.analyzeClass("seminar.dataflow.intervaltargets.IntervalAnalysis2", IntervalLauncher.IntervalAnalysisType.NAIVE);
        IntervalAnalysis intervalAnalysis = IntervalLauncher.getCurrentIntervalAnalysis();
        Return returnStmt = findReturnStmt(intervalAnalysis.outSet.keySet());
        IntervalAbstractState stateForReturn = intervalAnalysis.outSet.get(returnStmt);
        Assertions.assertEquals("[7,7]", stateForReturn.getIntervalForVariable("x").toString());
        Assertions.assertEquals("[0,0]", stateForReturn.getIntervalForVariable("y").toString());
        Assertions.assertEquals("[7,7]", stateForReturn.getIntervalForVariable("z").toString());
        Assertions.assertEquals("[7,7]", stateForReturn.getIntervalForVariable("w").toString());
        Assertions.assertEquals("[14,14]", stateForReturn.getIntervalForVariable("v").toString());
    }

    @Test
    public void testWideningAssignInfinityIfOutOfProgramConstants() {
        IntervalLauncher.analyzeClass("seminar.dataflow.intervaltargets.IntervalAnalysis3", IntervalLauncher.IntervalAnalysisType.WIDENING);
        IntervalAnalysis intervalAnalysis = IntervalLauncher.getCurrentIntervalAnalysis();
        Return returnStmt = findReturnStmt(intervalAnalysis.outSet.keySet());
        IntervalAbstractState stateForReturn = intervalAnalysis.outSet.get(returnStmt);
        Assertions.assertEquals("[7,∞]", stateForReturn.getIntervalForVariable(returnedVariable(returnStmt)).toString());
        Assertions.assertEquals("[0,∞]", stateForReturn.getIntervalForVariable("y").toString());
        Assertions.assertEquals("[-∞,3]", stateForReturn.getIntervalForVariable("k").toString());
    }

    @Test
    public void testNarrowingAdjustVariablesProperly() {
        IntervalLauncher.analyzeClass("seminar.dataflow.intervaltargets.IntervalAnalysis3", IntervalLauncher.IntervalAnalysisType.NARROWING);
        IntervalAnalysis intervalAnalysis = IntervalLauncher.getCurrentIntervalAnalysis();
        Return returnStmt = findReturnStmt(intervalAnalysis.outSet.keySet());
        IntervalAbstractState stateForReturn = intervalAnalysis.outSet.get(returnStmt);
        Assertions.assertEquals("[8,8]", stateForReturn.getIntervalForVariable(returnedVariable(returnStmt)).toString());
        Assertions.assertEquals("[0,∞]", stateForReturn.getIntervalForVariable("y").toString());
        Assertions.assertEquals("[-∞,3]", stateForReturn.getIntervalForVariable("k").toString());

    }
}
