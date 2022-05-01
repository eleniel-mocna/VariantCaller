package cz.cuni.mff.soukups3.VariantCaller;

public interface VariantsManager {
    public void receiveVariant(Variant variant);
    public default void receiveVariant(Variant[] variants){
        for (Variant variant:
             variants) {
            receiveVariant(variant);
        }
    }
    public Iterable<Variant> output();
}
