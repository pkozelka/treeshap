package ai.h2o.treeshap.optimized;

import ai.h2o.treeshap.tree.PkNode;
import ai.h2o.treeshap.tree.PkTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapOptimized {
    private final double[] data;
    private final PkTree tree;
    private final double expectedValue;
    private final int pathArrayCapacity;
    // temporary variables, for use inside recursion - minimizing number of passed parameters
    private List<PathElement> pathArray;
    private double[] phi;


    public ShapOptimized(double[] data, PkTree tree, double expectedValue) {
        this.data = data;
        this.tree = tree;
        this.expectedValue = expectedValue;
        final int maxd = maxDepth(tree.root) + 2;
        this.pathArrayCapacity = (maxd * (maxd + 1)) / 2;
    }

    private static int maxDepth(PkNode node) {
        if (node.isLeaf()) return 0;
        return Math.max(maxDepth(node.yes) + 1, maxDepth(node.no) + 1);
    }

    private static ArrayList<PathElement> createPathArray(int capacity) {
        final ArrayList<PathElement> pathArray = new ArrayList<>(capacity);
        // note that element at index [0] seems to never be accessed (we would get NPE otherwise)
        int boundary = 0;
        int cnt = 0;
        for (int i = 0; i < capacity; i++) {
            if (cnt == boundary) {
                // security measure - these bounds should never be touched!
                final PathElement separator = new PathElement();
                separator.featureIndex = -888;
//                final PathElement separator = null;
                pathArray.add(separator);
                cnt = 0;
                boundary++;
            } else {
                pathArray.add(new PathElement());
                cnt++;
            }
        }
        return pathArray;
    }

    private void treeShap(final PkNode node, /*NOT final*/ int uniqueDepth, final int parentUniquePathPtr,
                          final double parentZeroFraction, final double parentOneFraction, final int parentFeatureIndex,
                          final double conditionFraction) {
        // stop if we have no weight coming down to us
        if (conditionFraction == 0) return;

        // extend the unique path
        int uniquePathPtr = parentUniquePathPtr + uniqueDepth + 1;
        stdcopy(parentUniquePathPtr, parentUniquePathPtr + uniqueDepth, uniquePathPtr); //TODO this +1 in second param looks to be AT LEAST useless, MAYBE even harmful. Explore when tests are working!
        extendPath(uniquePathPtr, uniqueDepth, parentZeroFraction, parentOneFraction, parentFeatureIndex);

        if (node.isLeaf()) {
            // leaf node
            for (int i = 1; i <= uniqueDepth; ++i) {
                final double w = unwoundPathSum(uniquePathPtr, uniqueDepth, i);
                final PathElement el = pathArray.get(uniquePathPtr + i);
                final double contrib = w * (el.oneFraction - el.zeroFraction) * node.leafValue * conditionFraction;
                phi[el.featureIndex] += contrib;
            }
        } else {
            // internal node
            final double featureValue = data[node.splitFeatureIndex];
            final boolean decision = featureValue <= node.splitValue;
            final PkNode hotNode = decision ? node.yes : node.no;
            final PkNode coldNode = decision ? node.no : node.yes;
            final double hotZeroFraction = hotNode.dataCount / node.dataCount;
            final double coldZeroFraction = coldNode.dataCount / node.dataCount;
            double incomingZeroFraction = 1;
            double incomingOneFraction = 1;

            // see if we have already split on this feature,
            final Integer pathIndex = findFeatureSplit(uniquePathPtr, uniqueDepth, node.splitFeatureIndex);
            if (pathIndex != null) {
                // if so we undo that split so we can redo it for this node
                final PathElement el = pathArray.get(uniquePathPtr + pathIndex);
                incomingZeroFraction = el.zeroFraction;
                incomingOneFraction = el.oneFraction;
                unwindPath(uniquePathPtr, uniqueDepth, pathIndex);
                uniqueDepth -= 1;
            }

            treeShap(hotNode, uniqueDepth + 1, uniquePathPtr,
                hotZeroFraction * incomingZeroFraction, incomingOneFraction,
                node.splitFeatureIndex, conditionFraction);

            treeShap(coldNode, uniqueDepth + 1, uniquePathPtr,
                coldZeroFraction * incomingZeroFraction, 0,
                node.splitFeatureIndex, conditionFraction);

        }
    }

    private Integer findFeatureSplit(final int uniquePathPtr, final int uniqueDepth, final int splitIndex) {
        int pathIndex = 0;
        for (; pathIndex <= uniqueDepth; ++pathIndex) {
            final PathElement el = pathArray.get(uniquePathPtr + pathIndex);
            if (el.featureIndex == splitIndex) {
                return pathIndex;
            }
        }
        return null;
    }

    public double[] calculateContributions() {
        final int icols = data.length;
        final int ncolumns = icols + 1;
        final double[] contributions = new double[ncolumns];
        Arrays.fill(contributions, -0.0);
        contributions[icols] += expectedValue;
        this.pathArray = createPathArray(pathArrayCapacity);
        this.phi = contributions;
        treeShap(tree.root, 0, 0, 1, 1, -1, 1);
        return contributions;
    }

    /**
     * Ala C++ std::copy - see http://www.cplusplus.com/reference/algorithm/copy/
     * (Copies the elements in the range [first,last) into the range beginning at result.)
     */
    private void stdcopy(int first, int last, int result) {
        while (first != last) {
            final PathElement src = pathArray.get(first);
            final PathElement dest = pathArray.get(result);
            dest.featureIndex = src.featureIndex;
            dest.zeroFraction = src.zeroFraction;
            dest.oneFraction = src.oneFraction;
            dest.pweight = src.pweight;
            ++result;
            ++first;
        }
    }

    // extend our decision path with a fraction of one and zero extensions
    private void extendPath(int uniquePathPtr, int uniqueDepth, double zeroFraction, double oneFraction, int featureIndex) {
        final PathElement el = pathArray.get(uniquePathPtr + uniqueDepth);
        el.featureIndex = featureIndex;
        el.zeroFraction = zeroFraction;
        el.oneFraction = oneFraction;
        el.pweight = uniqueDepth == 0 ? 1.0 : 0.0;
        for (int i = uniqueDepth - 1; i >= 0; i--) {
            final PathElement upi1 = pathArray.get(uniquePathPtr + i + 1);
            final PathElement upi = pathArray.get(uniquePathPtr + i);
            upi1.pweight += oneFraction * upi.pweight * (i + 1) / (double) (uniqueDepth + 1);
            upi.pweight = zeroFraction * upi.pweight * (uniqueDepth - i) / (double) (uniqueDepth + 1);
        }
    }

    // undo a previous extension of the decision path
    private void unwindPath(int uniquePathPtr, int uniqueDepth, int pathIndex) {
        final double oneFraction = pathArray.get(uniquePathPtr + pathIndex).oneFraction;
        final double zeroFraction = pathArray.get(uniquePathPtr + pathIndex).zeroFraction;
        double nextOnePortion = pathArray.get(uniquePathPtr + uniqueDepth).pweight;

        for (int i = uniqueDepth - 1; i >= 0; --i) {
            final PathElement el = pathArray.get(uniquePathPtr + i);
            final double d = (double) (uniqueDepth - i) / (double) (uniqueDepth + 1);
            if (oneFraction != 0) {
                final double tmp = el.pweight;
                el.pweight = nextOnePortion * ((double) (uniqueDepth + 1)) / ((i + 1) * oneFraction);
                nextOnePortion = tmp - el.pweight * zeroFraction * d;
            } else {
                el.pweight = (el.pweight / zeroFraction) / d;
            }
        }

        for (int i = pathIndex; i < uniqueDepth; ++i) {
            final PathElement el = pathArray.get(uniquePathPtr + i);
            final PathElement el1 = pathArray.get(uniquePathPtr + i + 1);
            el.featureIndex = el1.featureIndex;
            el.zeroFraction = el1.zeroFraction;
            el.oneFraction = el1.oneFraction;
        }
    }

    // determine what the total permutation weight would be if
    // we unwound a previous extension in the decision path
    private double unwoundPathSum(int uniquePathPtr, int uniqueDepth, int pathIndex) {
        final double oneFraction = pathArray.get(uniquePathPtr + pathIndex).oneFraction;
        final double zeroFraction = pathArray.get(uniquePathPtr + pathIndex).zeroFraction;
        double nextOnePortion = pathArray.get(uniquePathPtr + uniqueDepth).pweight;
        double total = 0;
        for (int i = uniqueDepth - 1; i >= 0; --i) {
            final PathElement el = pathArray.get(uniquePathPtr + i);
            final double tmp;
            final double d = (double) (uniqueDepth - i) / (double) (uniqueDepth + 1);
            if (oneFraction != 0) {
                tmp = nextOnePortion * (uniqueDepth + 1) / ((i + 1) * oneFraction);
                nextOnePortion = el.pweight - tmp * zeroFraction * d;
            } else {
                tmp = (el.pweight / zeroFraction) / d;
            }
            total += tmp;
        }
        return total;
    }

    /*
     data we keep about our decision path
     note that pweight is included for convenience and is not tied with the other
     attributes the pweight of the i'th path element is the permutation weight of
     paths with i-1 ones in them
    */
    static class PathElement {
        public int featureIndex = -777;
        public double zeroFraction = -777.777;
        public double oneFraction = -777.777;
        public double pweight = -777.777;

        @Override
        public String toString() {
            if (featureIndex == -888) {
                return "- - - -";
            }
            return "PE{" +
                (featureIndex == -777 ? "-" :
                    "f=" + featureIndex +
                        ", zf=" + zeroFraction +
                        ", of=" + oneFraction +
                        ", pw=" + pweight
                ) + '}';
        }
    }
}
