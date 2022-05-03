package cz.cuni.mff.soukups3.VariantCaller;

public interface VariantsManager {
    void reportVariant(Variant variant,
                       boolean forwardCovered,
                       boolean forward,
                       boolean reverseCovered,
                       boolean reverse);
    default void reportVariant(Variant[] variants,
                               boolean[] forwardCovereds,
                               boolean[] forwards,
                               boolean[] reverseCovereds,
                               boolean[] reverses){
        for (int i = 0; i < variants.length; i++) {
            reportVariant(variants[i],
                            forwardCovereds[i],
                            forwards[i],
                            reverseCovereds[i],
                            reverses[i]);
        }
    }
    void reportQualityCoverage(String chrom, int pos);
    void reportCoverage(String chrom, int pos);
    int getCoverage(String chrom, int pos);
    int getQualityCoverage(String chrom, int pos);
    Iterable<VariantStats> output();
}
