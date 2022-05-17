package cz.cuni.mff.soukups3.VariantCaller;

public interface VariantsManager {
    /**
     * Register the provided variant with the given stats.
     * @param variant The found variant
     * @param forwardCovered Is this variant supported in the forward strand and covered by reverse strand,
     *                       but NOT supported by the latter?
     * @param forward Is this variant supported in the forward strand and NOT covered by reverse strand?
     * @param reverseCovered Is this variant supported in the reverse strand and covered by forward strand,
     *                       but NOT supported by the latter?
     * @param reverse Is this variant supported in the reverse strand and NOT covered by forward strand?
     */
    void reportVariant(Variant variant,
                       boolean forwardCovered,
                       boolean forward,
                       boolean reverseCovered,
                       boolean reverse);

    /**
     * Add one more quality passing coverage by quality reads at the given location.
     * @param chrom The chromosome of the location
     * @param pos The position in the chromosome (1-based as the Reads are)
     */
    void reportQualityCoverage(String chrom, int pos);
    /**
     * Add one more coverage by quality reads at the given location.
     * @param chrom The chromosome of the location
     * @param pos The position in the chromosome (1-based as the Reads are)
     */
    void reportCoverage(String chrom, int pos);
    /**
     * Get the coverage at the given location.
     * @param chrom The chromosome of the location
     * @param pos The position in the chromosome (1-based as the Reads are)
     * @return The coverage at the location
     */
    int getCoverage(String chrom, int pos);
    /**
     * Get the coverage passing quality at the given location.
     * @param chrom The chromosome of the location
     * @param pos The position in the chromosome (1-based as the Reads are)
     * @return The coverage passing quality at the location
     */
    int getQualityCoverage(String chrom, int pos);
    Iterable<VariantStats> output();
}
