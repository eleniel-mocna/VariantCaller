package cz.cuni.mff.soukups3.VariantCaller;

/**
 * Class containing all stats for one Variant.
 */
public class VariantStats implements Comparable<VariantStats> {
    public final Variant variant;
    public int forwardPaired = 0;
    public int forwardUnpaired = 0;
    public int reversePaired = 0;
    public int reverseUnpaired = 0;
    public int paired = 0;

    /**
     * @param variant Create a VariantStats around a variant
     *                with empty stats.
     */
    public VariantStats(Variant variant){
        this.variant = variant;
    }

    /**
     * Add statistics for this variant from one Reads group
     * @param forwardCovered Is this variant supported in the forward strand and covered by reverse strand,
     *                       but NOT supported by the latter?
     * @param forward Is this variant supported in the forward strand and NOT covered by reverse strand?
     * @param reverseCovered Is this variant supported in the reverse strand and covered by forward strand,
     *                       but NOT supported by the latter?
     * @param reverse Is this variant supported in the reverse strand and NOT covered by forward strand?
     */
    public void addStats(boolean forwardCovered,
                         boolean forward,
                         boolean reverseCovered,
                         boolean reverse){
        if (forward && reverse) {
            this.paired+=1;
            return;
        }
        if (forward && reverseCovered){
            forwardPaired+=1;
        }
        else if (forward){
            forwardUnpaired+=1;
        }
        if (reverse && forwardCovered){
            reversePaired+=1;
        }
        else if (reverse){
            reverseUnpaired+=1;
        }
    }

    /**
     * @param stats Variant to be compared to
     * @return In chromosome comparison (this.pos - stats.pos)
     */
    @Override
    public int compareTo(VariantStats stats) {
        if (stats.getClass().equals(VariantStats.class)){
            return variant.pos- stats.variant.pos;
        }
        System.err.println("WARNING: VariantStats compared unpredictably!");
        return variant.hashCode()-stats.hashCode();
    }

    @Override
    public String toString() {
        return "VariantStats{" +
                "variant=" + variant +
                ", forwardPaired=" + forwardPaired +
                ", forwardUnpaired=" + forwardUnpaired +
                ", reversePaired=" + reversePaired +
                ", reverseUnpaired=" + reverseUnpaired +
                ", paired=" + paired +
                '}';
    }
}
