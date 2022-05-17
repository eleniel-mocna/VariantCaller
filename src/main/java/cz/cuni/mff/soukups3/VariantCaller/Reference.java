package cz.cuni.mff.soukups3.VariantCaller;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class for managing the reference, allowing easily access bases at given location.
 */
public class Reference {
    private final LinkedHashMap<String, Character[]> bases;
    private final List<String> chromNames;

    /**
     * Create a reference from a builder.
     * @param builder Builder which prepared everything for this Reference
     */
    public Reference(ReferenceBuilder builder){
        bases=builder.getBases();
        chromNames = bases.keySet().stream().toList();
    }

    /**
     * Returns the index of the given chromosome or -1 if there is none
     * @param chrom the name of the chromosome
     * @return the position of the chromosome
     */
    public int getChromIndex(String chrom){
        return chromNames.indexOf(chrom);
    }

    /**
     * Get base at given location.
     * @param chrom Chromosome of the given location.
     * @param pos 1-based position of the given location (equals to the position from SAM file)
     * @return The reference base at the given location.
     */
    public char getBase(String chrom, int pos){
        return bases.get(chrom)[pos];
    }

    /**
     * Return all bases from the first location (inclusive) up to the end (exclusive)
     * @param chrom Chromosome location
     * @param first Begin position (inclusive)
     * @param end End position (exclusive)
     * @return All the bases.
     */
    public String getBases(String chrom, int first, int end){
        StringBuilder ret = new StringBuilder();
        for (int i = first; i < end; i++) {
            ret.append(getBase(chrom, i));
        }
        return ret.toString();
    }

    /**
     * Get all chromosome names order as they came in.
     * @return The chromosome names
     */
    public String[] getChromosomes(){
        return bases.keySet().toArray(new String[0]);
    }
}
