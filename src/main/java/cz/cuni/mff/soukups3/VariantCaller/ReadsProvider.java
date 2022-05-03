package cz.cuni.mff.soukups3.VariantCaller;

public interface ReadsProvider {
    Read[] getNext();
    boolean hasNext();
}
