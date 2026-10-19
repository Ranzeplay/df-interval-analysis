package seminar.dataflow.intervaltargets;

/**
 * For this target we check that TOP holds for received parameters
 * and that x is a singleton (the range [5,5]).
 */
public class IntervalAnalysis1 {
    public static int func(int m, int n) {
        int x = 5;
        return x;
    }

}
