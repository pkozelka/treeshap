package ai.h2o.treeshap.pkimpl;

/**
 * Path element for use in {@link ShapAlgo2}
 */
public class PathElement {
    /**
     * (m.d)
     */
    public Integer featureIndex = null;
    /**
     * (m.z)
     */
    public double zeroFraction = Double.NaN;
    /**
     * (m.o)
     */
    public double oneFraction = Double.NaN;
    /**
     * (m.w)
     */
    public double weight = Double.NaN;

    @Override
    public String toString() {
        return "PE{f=" + featureIndex +
            ", zf=" + zeroFraction +
            ", of=" + oneFraction +
            ", pw=" + weight + '}';
    }
}
