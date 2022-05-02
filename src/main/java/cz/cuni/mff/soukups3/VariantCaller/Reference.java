package cz.cuni.mff.soukups3.VariantCaller;

import java.util.HashMap;

public class Reference {
    private final HashMap<String, Character[]> bases;
    public Reference(ReferenceBuilder builder){
        bases=builder.getBases();
    }
    public char getBase(String chrom, int pos){
        return bases.get(chrom)[pos];
    }
}
