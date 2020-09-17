package ai.h2o.treeshap.data;

import ai.h2o.treeshap.tree.PkNode;
import ai.h2o.treeshap.tree.PkTree;
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
        PkNode.split(/*dc=*/1710.00, /*col=*/12, 0X1.7800000000001P3,
            PkNode.split(/*dc=*/99.0000, /*col=*/0, 0X1.7599990000001P3,
                PkNode.split(/*dc=*/78.0000, /*col=*/8, 0X1.22D0E50000001P-5,
                    PkNode.leaf(/*dc=*/18.0000, 0X1.A8D1FB9AB9583P-7),
                    PkNode.leaf(/*dc=*/60.0000, 0X1.3DF1F397EB9E1P-4)),
                PkNode.leaf(/*dc=*/21.0000, -0X1.85260DB821DF8P-8)),
            PkNode.split(/*dc=*/1611.00, /*col=*/11, 0X1.24CCCD0000001P3,
                PkNode.split(/*dc=*/1589.00, /*col=*/9, 0X1.28F5C30000001P-3,
                    PkNode.split(/*dc=*/36.0000, /*col=*/13, 0X1.9B851F0000001P1,
                        PkNode.leaf(/*dc=*/16.0000, 0X1.B19DAEF991289P-5),
                        PkNode.leaf(/*dc=*/20.0000, 0X1.6A91DA1982117P-9)),
                    PkNode.split(/*dc=*/1553.00, /*col=*/6, 0X1.0C268C0000001P-6,
                        PkNode.split(/*dc=*/452.000, /*col=*/7, 0X1.345F880000001P-6,
                            PkNode.split(/*dc=*/132.000, /*col=*/2, 0X1.5C98820000001P-6,
                                PkNode.leaf(/*dc=*/56.0000, -0X1.0902FB8AE6482P-7),
                                PkNode.split(/*dc=*/76.0000, /*col=*/4, 0X1.C7DB210000001P-9,
                                    PkNode.split(/*dc=*/39.0000, /*col=*/12, 0X1.4400000000001P5,
                                        PkNode.leaf(/*dc=*/20.0000, 0X1.05FAB062F659EP-4),
                                        PkNode.leaf(/*dc=*/19.0000, 0X1.B6153B6FA515FP-9)),
                                    PkNode.leaf(/*dc=*/37.0000, -0X1.DD30976B70317P-8))),
                            PkNode.split(/*dc=*/320.000, /*col=*/9, 0X1.F5C28F0000001P-3,
                                PkNode.split(/*dc=*/44.0000, /*col=*/6, 0X1.5C98830000001P-7,
                                    PkNode.leaf(/*dc=*/27.0000, -0X1.AF0BAAB653CDCP-8),
                                    PkNode.leaf(/*dc=*/17.0000, 0X1.7ADC786734A6AP-6)),
                                PkNode.split(/*dc=*/276.000, /*col=*/2, 0X1.7916340000001P-5,
                                    PkNode.leaf(/*dc=*/257.000, -0X1.3F62593FECDAEP-7),
                                    PkNode.leaf(/*dc=*/19.0000, 0X1.BB15D5D8E3689P-9)))),
                        PkNode.split(/*dc=*/1101.00, /*col=*/12, 0X1.CC00000000001P5,
                            PkNode.split(/*dc=*/981.000, /*col=*/7, 0X1.198EE00000001P-8,
                                PkNode.split(/*dc=*/35.0000, /*col=*/16, 0X1.E200000000001P6,
                                    PkNode.leaf(/*dc=*/16.0000, 0X1.DD3B00362F961P-7),
                                    PkNode.leaf(/*dc=*/19.0000, -0X1.74855A3A6B388P-8)),
                                PkNode.split(/*dc=*/946.000, /*col=*/13, 0X1.A851EB0000001P1,
                                    PkNode.leaf(/*dc=*/871.000, -0X1.4CE24337ABF15P-7),
                                    PkNode.split(/*dc=*/75.0000, /*col=*/12, 0X1.7800000000001P4,
                                        PkNode.leaf(/*dc=*/20.0000, 0X1.80DF228C4B205P-7),
                                        PkNode.leaf(/*dc=*/55.0000, -0X1.07A991EF5594EP-7)))),
                            PkNode.split(/*dc=*/120.000, /*col=*/4, 0X1.BA72CE0000001P-8,
                                PkNode.split(/*dc=*/95.0000, /*col=*/16, 0X1.AF00000000001P7,
                                    PkNode.leaf(/*dc=*/79.0000, -0X1.1AA909BEB889FP-7),
                                    PkNode.leaf(/*dc=*/16.0000, 0X1.3431FBDE8C097P-8)),
                                PkNode.leaf(/*dc=*/25.0000, 0X1.11E59A870AA1CP-6))))),
                PkNode.leaf(/*dc=*/22.0000, 0X1.5DDAEF069BA5AP-5)))
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
