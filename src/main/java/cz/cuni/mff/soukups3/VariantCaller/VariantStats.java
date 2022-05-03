package cz.cuni.mff.soukups3.VariantCaller;

public class VariantStats implements Comparable {
    public Variant variant;
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

    @Override
    public int compareTo(Object o) {
        if (o.getClass().equals(VariantStats.class)){

            return variant.pos-((VariantStats) o).variant.pos;
        }
        System.err.println("WARNING: VariantStats compared unpredictably!");
        return variant.hashCode()-o.hashCode();
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
