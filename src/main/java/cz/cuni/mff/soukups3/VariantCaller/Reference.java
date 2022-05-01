package cz.cuni.mff.soukups3.VariantCaller;

import java.util.HashMap;

public class Reference {
    // TODO: Add support for arrays bigger then Integer
    private final HashMap<String, Character[]> bases;
    public Reference(ReferenceBuilder builder){
        bases=builder.getBases();
    }
//    public position2Index
}
