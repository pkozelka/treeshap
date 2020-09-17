package ai.h2o.treeshap.optimized;

import ai.h2o.treeshap.data.TestDataConstants;
import ai.h2o.treeshap.tree.PkTree;
import org.junit.Test;

public class ShapOptimizedTest {
    @Test
    public void testShapOptimized() {
        final PkTree tree = TestDataConstants.MMEXAMPLE_TREE7;
        final double[] data = TestDataConstants.MMEXAMPLE_ROW_DOUBLE;
        final ShapOptimized so = new ShapOptimized(data, tree, 0);
        final double[] predictContribs = so.calculateContributions();
        for (int i = 0; i < data.length; i++) {
            System.out.printf("data[%2d] = %12.6f, contributes: %14.10f%n", i, data[i], predictContribs[i]);
        }
    }
}
