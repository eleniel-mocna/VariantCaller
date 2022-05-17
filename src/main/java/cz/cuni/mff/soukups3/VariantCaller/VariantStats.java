package cz.cuni.mff.soukups3.VariantCaller;

public class VariantStats implements Comparable<VariantStats> {
    public final Variant variant;
    public int forwardPaired = 0;
    public int forwardUnpaired = 0;
    public int reversePaired = 0;
    public int reverseUnpaired = 0;
    public int paired = 0;

    public VariantStats(Variant variant){

        this.variant = variant;
    }
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
