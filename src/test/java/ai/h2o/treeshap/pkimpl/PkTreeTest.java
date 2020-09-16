package ai.h2o.treeshap.pkimpl;

import ai.h2o.treeshap.data.TestDataConstants;
import org.junit.Test;

public class PkTreeTest {

    @Test
    public void testShapAlgo2() {
        final PkTree tree = TestDataConstants.MMEXAMPLE_TREE7;
        final double[] data = TestDataConstants.MMEXAMPLE_ROW_DOUBLE;
        final double[] predictContribs = ShapAlgo2.compute(data, tree);
        for (int i = 0; i < predictContribs.length; i++) {
            System.out.printf("data[%2d] = %12.6f, contributes: %14.10f%n", i, data[i], predictContribs[i]);
        }
    }
}
