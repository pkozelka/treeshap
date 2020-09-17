package ai.h2o.treeshap.pkimpl;

import ai.h2o.treeshap.tree.PkNode;
import ai.h2o.treeshap.tree.PkTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rewrite of the Algorithm 2 from [1].
 *
 * The main goal here is to get "fanatically faithful" transcript of the algo into java.
 * Therefore, performance and memory optimizations are neither done nor planned here.
 *
 * Deviations from original notation:
 * - java list/arrays are zero-based
 * - tree is represented by recursive structure of nodes, each is either a leaf or a split node with yes, no, and other props
 * - the path (stored in variable "m") has long-named properties to make the thing more readable
 *
 * Bugfixes:
 * - [bugfix1] in EXTEND, iteration misses newly added element, causing wrong computation - see [2]
 * - [bugfix2] in UNWIND, iteration exceeds into removed element, causing exception
 *
 * Resources:
 * - [1] Consistent Individualized Feature Attribution for Tree Ensembles - https://arxiv.org/pdf/1802.03888.pdf
 * - [2] From local explanations to global understanding with explainable AI for trees, pages 65,66 - https://www.nature.com/articles/s42256-019-0138-9.epdf?shared_access_token=RCYPTVkiECUmc0CccSMgXtRgN0jAjWel9jnR3ZoTv0O81kV8DqPb2VXSseRmof0Pl8YSOZy4FHz5vMc3xsxcX6uT10EzEoWo7B-nZQAHJJvBYhQJTT1LnJmpsa48nlgUWrMkThFrEIvZstjQ7Xdc5g%3D%3D
 */
public class ShapAlgo2 {
    static boolean DEBUG = true;
    private String indent = "";

    private final double[] phi;
    private final double[] x;

    public static double[] compute(double[] x, PkTree tree) {
        final ShapAlgo2 shap = new ShapAlgo2(x);
        shap.recurse(tree.root, new ArrayList<PathElement>(), 1.0, 1.0, null);
        return shap.phi;
    }

    private ShapAlgo2(double[] x) {
        this.x = x;
        this.phi = new double[x.length];
        Arrays.fill(phi, -0.0);
    }

    private void recurse(PkNode j, List<PathElement> m, double pz, double po, Integer pi) {
        if (DEBUG) System.out.printf("%srecurse(DC=%s, P0f=%f, P1f=%f, PFi=%d)%n", indent, j.dataCount, pz, po, pi);
        m = extend(m, pz, po, pi);
        if (j.isLeaf()) {
            for (int i = 1; i < m.size() - 1; i++) { // note that we are skipping first element
                double w = sumWeight(unwind(m, i));
                final PathElement mi = m.get(i);
                final double contrib = w * (mi.oneFraction - mi.zeroFraction) * j.leafValue;
                if (DEBUG) System.out.printf("%s* phi[%2d] += %f ... w = %f%n", indent, mi.featureIndex, contrib, w);
                phi[mi.featureIndex] += contrib;
            }
        } else {
            final boolean isHot = x[j.splitFeatureIndex] <= j.splitValue; // evaluate the decision
            final PkNode h = isHot ? j.yes : j.no;
            final PkNode c = isHot ? j.no : j.yes;
            double iz = 1;
            double io = 1;
            final Integer k = findFirst(m, j.splitFeatureIndex);
            if (k != null) {
                final PathElement mk = m.get(k);
                iz = mk.zeroFraction;
                io = mk.oneFraction;
                m = unwindTracked(m, k);
            }
            final String indentBackup = indent;
            indent += "    ";
            recurse(h, m, iz * h.dataCount / j.dataCount, io, j.splitFeatureIndex);
            recurse(c, m, iz * c.dataCount / j.dataCount, 0, j.splitFeatureIndex);
            indent = indentBackup;
        }
    }

    private Integer findFirst(final List<PathElement> m, final int splitFeatureIndex) {
        int index = 0;
        for (PathElement e : m) {
            if (e.featureIndex != null && e.featureIndex.equals(splitFeatureIndex)) {
                return index;
            }
            index++;
        }
        return null;
    }

    private List<PathElement> extend(final List<PathElement> origM, final double pz, final double po, final Integer pi) {
        if (DEBUG) System.out.printf("%s(+) 0f=%f, 1f=%f, Fi=%d  -->  ", indent, pz, po, pi);
        // copy original
        final List<PathElement> m = copy(origM);
        // add new item
        final PathElement e = new PathElement();
        e.featureIndex = pi;
        e.zeroFraction = pz;
        e.oneFraction = po;
        e.weight = origM.isEmpty() ? 1.0 : 0.0;
        m.add(e);
        final int sz = m.size(); // use "sz" rather than "l" which is optically too similar to "1" (one)
        // distribute weights; note that the iteration works with "old" sz
        for (int i = sz - 2; i >= 0; i--) { // <-- [bugfix1] ... partly reverted, by assigning sz *after* new element is added
            final PathElement mi1 = m.get(i + 1);
            final PathElement mi = m.get(i);

            final int ii = i + 1; // this would be the index in one-based array

            final double d = (double) ii / (double) sz;
            mi1.weight = mi1.weight + po * mi.weight * d;

            final double dd = (double) (sz - ii) / (double) sz;
            mi.weight = mi.weight * (pz * dd);
        }
        if (DEBUG) System.out.printf("(%d)%s%n", m.size(), m);
        return m;
    }

    /**
     * Removes i-th element of the path, and redistributes all weights
     * @param origM elements of decision path
     * @param i  zero-based index to be removed
     * @return transformed decision path
     */
    private static List<PathElement> unwind(List<PathElement> origM, int i) {
        final int sz = origM.size(); // use "sz" rather than "l" which is optically too similar to "1" (one)
        double n = origM.get(sz-1).weight; // last weight
        // copy elements from original, all but the last one
        final List<PathElement> m = copy(origM);
        m.remove(m.size() - 1);
        // redistribute weights
        final PathElement mi = m.get(i);
        for (int j = sz - 2; j > 0; j--) {
            final PathElement mj = m.get(j);
            final int jj = j + 1; // this would be the index in one-based array
            if (mi.oneFraction != 0.0) {
                final double t = mj.weight;
                mj.weight = n * sz / (jj * mi.oneFraction);
                final double dd = (double) (sz - jj) / (double) sz;
                n = t - mj.weight * mi.zeroFraction * dd;
            } else {
                final double d = (double) (sz - jj);
                mj.weight = mj.weight * sz / (mj.zeroFraction * d);
            }
        }
        // shift other attributes
        for (int j = i; j < sz - 2; j++) { // <-- [bugfix2]
            final PathElement mj = m.get(j);
            final PathElement mj1 = m.get(j + 1);
            mj.featureIndex = mj1.featureIndex;
            mj.zeroFraction = mj1.zeroFraction;
            mj.oneFraction = mj1.oneFraction;
        }
        return m;
    }

    private List<PathElement> unwindTracked(List<PathElement> origM, int i) {
        if (DEBUG) System.out.printf("%s(-) Pi=%d  -->  ", indent, i);
        final List<PathElement> m = unwind(origM, i);
        if (DEBUG) System.out.printf("(%d)%s%n", m.size(), m);
        return m;
    }

    private static List<PathElement> copy(final List<PathElement> origM) {
        final List<PathElement> m = new ArrayList<>();
        for (final PathElement e : origM) {
            final PathElement cloned = new PathElement();
            cloned.featureIndex = e.featureIndex;
            cloned.zeroFraction = e.zeroFraction;
            cloned.oneFraction = e.oneFraction;
            cloned.weight = e.weight;
            m.add(cloned);
        }
        return m;
    }

    private static double sumWeight(final List<PathElement> m) {
        double sum = 0;
        for (PathElement e : m) {
            sum += e.weight;
        }
        return sum;
    }
}
