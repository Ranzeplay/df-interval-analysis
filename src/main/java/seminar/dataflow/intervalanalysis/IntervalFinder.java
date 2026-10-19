package seminar.dataflow.intervalanalysis;

import java.util.List;

public class IntervalFinder {

    /**
     * Finds the best fitting interval according to ω'([l,h]) = [max{i ∈ B | i ≤ l}, min{i ∈ B | h ≤ i}]
     *
     * @param lower lower bound of input interval
     * @param upper upper bound of input interval
     * @param B set of boundary points (must be sorted)
     * @return array with [lowerBound, upperBound] of best fitting interval, or null if no valid interval exists
     */
    public static int[] findBestFittingInterval(int lower, int upper, int[] B) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }

    /**
     * Convenience method that takes a List instead of array
     */
    public static int[] findBestFittingInterval(int lower, int upper, List<Integer> B) {
        if (B == null || B.isEmpty()) {
            return null;
        }

        // Convert to array and sort
        int[] sortedB = B.stream()
                .mapToInt(Integer::intValue)
                .sorted()
                .toArray();

        return findBestFittingInterval(lower, upper, sortedB);
    }

    public static int findBestSingleton(int value, List<Integer> B) {
        // TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}