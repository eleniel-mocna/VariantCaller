package cz.cuni.mff.soukups3.VariantCaller;

import java.util.Objects;

/**
 * Class for storing a variant without any stats.
 */
public class Variant {
    /**
     * Defines of which type the given Variant is.
     */
    public enum VariantType{
        INSERTION,
        DELETION,
        MISMATCH
    }

    public final VariantType type;
    public final String chrom;
    public final int pos;
    public String variantString;

    /**
     * @param type Which type is this variant
     * @param chrom At which chromosome
     * @param pos At which position
     * @param variantString The variant string:
     *                      Alt base for snps
     *                      Alt bases for insertions
     *                      Previous base + '\0'^{n-1} for deletions of length n
     */
    public Variant(VariantType type, String chrom, int pos, String variantString){
        this.type = type;
        this.chrom = chrom;
        this.pos = pos;
        this.variantString = variantString;
    }

    /**
     * Add a char to the variant string (used for indels)
     * @param c The char to be added
     */
    public void addToVariantString(char c){
        variantString += c;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "type=" + type +
                ", chrom='" + chrom + '\'' +
                ", pos=" + pos +
                ", variantString='" + variantString + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return pos == variant.pos
                && type == variant.type
                && chrom.equals(variant.chrom)
                && variantString.equals(variant.variantString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, chrom, pos, variantString);
    }
}
