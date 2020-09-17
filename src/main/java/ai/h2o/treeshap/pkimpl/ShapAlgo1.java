package ai.h2o.treeshap.pkimpl;

import ai.h2o.treeshap.tree.PkNode;
import ai.h2o.treeshap.tree.PkTree;

/**
 * See https://arxiv.org/pdf/1802.03888.pdf - Chapter 3.1, Algorithm 1.
 *
 * v - vector of node values; = "internal" for internal nodes
 * a,b - left and right node indexes for each internal node
 * t - thresholds for each internal node
 * d - indexes of the features used for splitting in internal nodes
 * r - cover of each node (ie. how many data samples fall in that sub-tree)
 * w - weight, measures the proportion of the training samples matching the conditioning set S fall into each leaf
 * s - set of non-zero indexes in z', ie. known features
 * z' - for each feature, 0 if unknown, 1 if known
 * x - feature values
 */
class ShapAlgo1 {
    double x[];
    double v[];
    double t[];
    float r[];
    int a[];
    int b[];
    int d[];

    /**
     * sincerely, I have no idea what this thing computes; I expected one computed contribution
     * weight per feature, and instead, this computes just one number.
     * Probably needs to be called repeatedly; more study of the paper needed here.
     */
    double expValue(Void x, Void s, Void tree /*v,a,b,t,r,d*/) {
        return g(1,1);
    }

    double g(int j, double w) {
        if (isLeaf(v[j])) {
            return w * v[j];
        }

        final double value = x[d[j]];
        if (!Double.isNaN(value)) {
            return value <= t[j]
                ? g(a[j], w)
                : g(b[j], w);
        }

        return g(a[j], w * r[a[j]] / r[j])
        +      g(b[j], w * r[b[j]] / r[j]);
    }


    private static boolean isLeaf(double leafValue) {
        return !Double.isNaN(leafValue);
    }

    double expValue(PkTree tree) {
        return g(tree.root, 1);
    }

    double g(PkNode node, double w) {
        if (node.isLeaf()) {
            return w * node.leafValue;
        }

        final double value = x[node.splitFeatureIndex];
        if (!Double.isNaN(value)) {
            // follow decision path
            return value <= node.splitValue
                ? g(node.yes, w)
                : g(node.no, w);
        }

        // take weighted average of both paths
        return g(node.yes, w * node.yes.dataCount / node.dataCount)
             + g(node.no,  w * node.no.dataCount / node.dataCount);
    }

}
