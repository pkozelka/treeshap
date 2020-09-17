package ai.h2o.treeshap.optimized;

import ai.h2o.treeshap.data.TestDataConstants;
import ai.h2o.treeshap.tree.PkTree;
import org.junit.Assert;
import org.junit.Test;

public class ShapOptimizedTest {
    @Test
    public void testShapOptimized() {
        final PkTree tree = TestDataConstants.MMEXAMPLE_TREE7;
        final double[] data = TestDataConstants.MMEXAMPLE_ROW_DOUBLE;
        final ShapOptimized so = new ShapOptimized(data, tree, 0);
        final double[] predictContribs = so.calculateContributions();
        double checksum = 0;
        for (int i = 0; i < data.length; i++) {
            System.out.printf("data[%2d] = %24A, contributes: %24A%n", i, data[i], predictContribs[i]);
            checksum += data[i] * 17 + predictContribs[i] * 29;
        }
        // this is to ensure that adjustments don't corrupt the examined computation
        Assert.assertEquals("0x1.10d29ad3269fap12", Double.toHexString(checksum));
    }
}
