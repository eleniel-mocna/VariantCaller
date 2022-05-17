package cz.cuni.mff.soukups3.VariantCaller;

/**
 * Interface which produces a String representation of collected variants.
 */
public interface ReportsWriter {
    /**
     * @param manager The manager storing all found variants
     * @return String with lineBreaks to be printed (wherever it is needed)
     */
    String writeVariants(VariantsManager manager);
}
