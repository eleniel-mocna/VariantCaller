package cz.cuni.mff.soukups3.VariantCaller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Variant {
    public enum VariantType{
        INSERTION,
        DELETION,
        MISMATCH
    }

    public VariantType type;
    public final String chrom;
    public final int pos;
    public String variantString;

    public Variant(VariantType type, String chrom, int pos, String variantString){
        this.type = type;
        this.chrom = chrom;
        this.pos = pos;
        this.variantString = variantString;
    }
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
