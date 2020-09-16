package ai.h2o.treeshap.data;

import ai.h2o.treeshap.pkimpl.PkNode;
import ai.h2o.treeshap.pkimpl.PkTree;
import java.io.PrintWriter;
import org.junit.Test;

/**
 * To achieve comparable results, all the algos should be tested with the data below.
 */
public class TestDataConstants {
    public static final float[] MMEXAMPLE_ROW_FLOAT = {8.800000190734863f, 0.27000001072883606f, 0.02536824904382229f, 0.03232405707240105f, 8.183306199498475E-4f, 0.047463174909353256f, 0.01391162071377039f, 0.040916528552770615f, 0.04500000178813934f, 0.36000001430511475f, 1.0010000467300415f, 7.0f, 45.0f, 3.0f, 20.700000762939453f, 0.44999998807907104f, 170.0f};

    public static final double[] MMEXAMPLE_ROW_DOUBLE = {8.800000190734863f, 0.27000001072883606f, 0.02536824904382229f, 0.03232405707240105f, 8.183306199498475E-4f, 0.047463174909353256f, 0.01391162071377039f, 0.040916528552770615f, 0.04500000178813934f, 0.36000001430511475f, 1.0010000467300415f, 7.0f, 45.0f, 3.0f, 20.700000762939453f, 0.44999998807907104f, 170.0f};

    // mmExample j=7 p=18 gid=1 {T20.43.json}
    public static final PkTree MMEXAMPLE_TREE7 = new PkTree(
        PkNode.split(/*dc=*/1710.0, /*col=*/12, 11.750000,
            PkNode.split(/*dc=*/99.0, /*col=*/0, 11.675000,
                PkNode.split(/*dc=*/78.0, /*col=*/8, 0.035500,
                    PkNode.leaf(/*dc=*/18.0, 0.012964),
                    PkNode.leaf(/*dc=*/60.0, 0.077623)),
                PkNode.leaf(/*dc=*/21.0, -0.005938)),
            PkNode.split(/*dc=*/1611.0, /*col=*/11, 9.150000,
                PkNode.split(/*dc=*/1589.0, /*col=*/9, 0.145000,
                    PkNode.split(/*dc=*/36.0, /*col=*/13, 3.215000,
                        PkNode.leaf(/*dc=*/16.0, 0.052932),
                        PkNode.leaf(/*dc=*/20.0, 0.002766)),
                    PkNode.split(/*dc=*/1553.0, /*col=*/6, 0.016367,
                        PkNode.split(/*dc=*/452.0, /*col=*/7, 0.018822,
                            PkNode.split(/*dc=*/132.0, /*col=*/2, 0.021277,
                                PkNode.leaf(/*dc=*/56.0, -0.008088),
                                PkNode.split(/*dc=*/76.0, /*col=*/4, 0.003478,
                                    PkNode.split(/*dc=*/39.0, /*col=*/12, 40.500000,
                                        PkNode.leaf(/*dc=*/20.0, 0.063960),
                                        PkNode.leaf(/*dc=*/19.0, 0.003342)),
                                    PkNode.leaf(/*dc=*/37.0, -0.007281))),
                            PkNode.split(/*dc=*/320.0, /*col=*/9, 0.245000,
                                PkNode.split(/*dc=*/44.0, /*col=*/6, 0.010638,
                                    PkNode.leaf(/*dc=*/27.0, -0.006577),
                                    PkNode.leaf(/*dc=*/17.0, 0.023124)),
                                PkNode.split(/*dc=*/276.0, /*col=*/2, 0.046031,
                                    PkNode.leaf(/*dc=*/257.0, -0.009747),
                                    PkNode.leaf(/*dc=*/19.0, 0.003380)))),
                        PkNode.split(/*dc=*/1101.0, /*col=*/12, 57.500000,
                            PkNode.split(/*dc=*/981.0, /*col=*/7, 0.004296,
                                PkNode.split(/*dc=*/35.0, /*col=*/16, 120.500000,
                                    PkNode.leaf(/*dc=*/16.0, 0.014564),
                                    PkNode.leaf(/*dc=*/19.0, -0.005684)),
                                PkNode.split(/*dc=*/946.0, /*col=*/13, 3.315000,
                                    PkNode.leaf(/*dc=*/871.0, -0.010159),
                                    PkNode.split(/*dc=*/75.0, /*col=*/12, 23.500000,
                                        PkNode.leaf(/*dc=*/20.0, 0.011745),
                                        PkNode.leaf(/*dc=*/55.0, -0.008046)))),
                            PkNode.split(/*dc=*/120.0, /*col=*/4, 0.006751,
                                PkNode.split(/*dc=*/95.0, /*col=*/16, 215.500000,
                                    PkNode.leaf(/*dc=*/79.0, -0.008626),
                                    PkNode.leaf(/*dc=*/16.0, 0.004703)),
                                PkNode.leaf(/*dc=*/25.0, 0.016717))))),
                PkNode.leaf(/*dc=*/22.0, 0.042707)))
    );

    @Test
    public void tree7Java() {
        MMEXAMPLE_TREE7.printTree(new PrintWriter(System.out));
    }

    @Test
    public void tree7JavaBuilder() {
        MMEXAMPLE_TREE7.printTreeConstructor(new PrintWriter(System.out));
    }
}
