package cz.cuni.mff.soukups3.VariantCaller;

/**
 * Interface for all classes that can provide Reads to Core.
 */
public interface ReadsProvider {

    /**
     * @return The next batch of Reads.
     */
    Read[] getNext();

    /**
     * @return Do we have some more Reads?
     */
    boolean hasNext();
}
