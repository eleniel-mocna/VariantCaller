package cz.cuni.mff.soukups3.VariantCaller;

import java.util.HashMap;
import java.util.Map;

public class Variant {
    private final String chrom;
    private final int pos;
    private final String variantString;
    private Map<String, Object> stats;

    public Variant(String chrom, int pos, String variantString){
        this.chrom = chrom;
        this.pos = pos;
        this.variantString = variantString;
        stats = new HashMap<>();
    }
    public void addStat(String key, Object value){
        stats.put(key, value);
    }

    @Override
    public String toString() {
        return "Variant{" +
                "chrom='" + chrom + '\'' +
                ", pos=" + pos +
                ", variantString='" + variantString + '\'' +
                ", stats=" + stats +
                '}';
    }
}
