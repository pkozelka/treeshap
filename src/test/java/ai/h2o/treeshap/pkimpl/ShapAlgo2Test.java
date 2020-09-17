package ai.h2o.treeshap.pkimpl;

import ai.h2o.treeshap.data.TestDataConstants;
import ai.h2o.treeshap.tree.PkTree;
import org.junit.Test;

public class ShapAlgo2Test {

    @Test
    public void testShapAlgo2() {
        final PkTree tree = TestDataConstants.MMEXAMPLE_TREE7;
        final double[] data = TestDataConstants.MMEXAMPLE_ROW_DOUBLE;
        final double[] predictContribs = ShapAlgo2.compute(data, tree);
        for (int i = 0; i < data.length; i++) {
            System.out.printf("data[%2d] = %24A, contrib: %24A%n", i, data[i], predictContribs[i]);
        }
    }
}
