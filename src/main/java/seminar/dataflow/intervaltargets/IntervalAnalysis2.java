package seminar.dataflow.intervaltargets;

/**
 * For this target we check that TOP does not appear if
 * we are operating from singletons (that don't exceed the overflow limit of ints)
 * and that singletons are kept.
 */
public class IntervalAnalysis2 {
    public static int func() {
        int y = 0;
        int x = 7;
        int z = x + y;
        int w = x - y;
        int v = z + w;
        return v;
    }

}
