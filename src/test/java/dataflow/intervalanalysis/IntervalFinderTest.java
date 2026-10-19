package dataflow.intervalanalysis;

import seminar.dataflow.intervalanalysis.IntervalAbstractValue;
import seminar.dataflow.intervalanalysis.IntervalFinder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntervalFinderTest {
    @Test
    public void findsTheCorrectEnclosingIntervalIfIntervalIsInDefinedBreaks() {
        int[] result = IntervalFinder.findBestFittingInterval(
                0, 5,
                new int[]{IntervalAbstractValue.NEG_INFINITY, -10, -5, 0, 5, 10, IntervalAbstractValue.POS_INFINITY}
        );
        assertArrayEquals(new int[]{0,5}, result);
    }

    @Test
    public void findsTheCorrectEnclosingIntervalIfIntervalIsInBreaks() {
        int[] result = IntervalFinder.findBestFittingInterval(
                0, 7,
                new int[]{IntervalAbstractValue.NEG_INFINITY, -10, -5, 0, 5, 10, IntervalAbstractValue.POS_INFINITY}
        );
        assertArrayEquals(new int[]{0,10}, result);
    }

    @Test
    public void findsTheCorrectEnclosingIntervalWithNegativesIfIntervalIsInBreaks() {
        int[] result = IntervalFinder.findBestFittingInterval(
                -12, 12,
                new int[]{IntervalAbstractValue.NEG_INFINITY, -10, -5, 0, 5, 10, IntervalAbstractValue.POS_INFINITY}
        );
        assertArrayEquals(new int[]{IntervalAbstractValue.NEG_INFINITY,IntervalAbstractValue.POS_INFINITY}, result);
    }
}