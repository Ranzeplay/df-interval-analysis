package seminar.dataflow.intervaltargets;

/**
 * For this target we will check divergence for non widening nor narrowing.
 * For the version of the analysis that implements them, it should converge
 */
public class IntervalAnalysis3 {
    public static int func() {
        int y = 0;
        int x = 7;
        int k = 3;
        x = x + 1;
        while(k > 0) {
            x = 7;
            x = x + 1;
            y = y + 1;
            k = k - 1;
        }
        return x;
    }

}
