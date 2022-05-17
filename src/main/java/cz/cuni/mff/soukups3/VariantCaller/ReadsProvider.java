package cz.cuni.mff.soukups3.VariantCaller;

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
