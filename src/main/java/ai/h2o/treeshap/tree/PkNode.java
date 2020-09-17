package ai.h2o.treeshap.tree;

public class PkNode {
    /**
     * (v) Leaf value. If not leaf, contains NAN.
     */
    public final double leafValue;

    /**
     * (t) Split value (threshold). If not split node, contains NAN.
     */
    public final double splitValue;

    /**
     * (a) left node
     */
    public final PkNode yes;

    /**
     * (b) right node
     */
    public final PkNode no;

    /**
     * (d) index of feature used to split
     */
    public final int splitFeatureIndex;

    /**
     * (r) cover
     */
    public final double dataCount;

    private PkNode(final double dataCount, final double leafValue, final int splitFeatureIndex, final double splitValue, final PkNode yes, final PkNode no) {
        this.dataCount = dataCount;
        this.leafValue = leafValue;
        this.splitValue = splitValue;
        this.splitFeatureIndex = splitFeatureIndex;
        this.yes = yes;
        this.no = no;
    }

    public static PkNode leaf(double dataCount, double leafValue) {
        return new PkNode(dataCount, leafValue, -1, Double.NaN, null, null);
    }

    public static PkNode split(double dataCount, int splitFeatureIndex, double splitValue, PkNode yes, PkNode no) {
        return new PkNode(dataCount, Double.NaN, splitFeatureIndex, splitValue, yes, no);
    }

    public boolean isSplit() {
        return Double.isNaN(leafValue);
    }

    public boolean isLeaf() {
        return !Double.isNaN(leafValue);
    }
}
