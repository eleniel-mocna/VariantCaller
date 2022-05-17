package cz.cuni.mff.soukups3.VariantCaller;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a default class implementing VariantsManager interface.
 */
public class DefaultVariantsManager implements VariantsManager{
    public String location2String(String chrom, int pos){
        return chrom+'\0'+pos;
    }
    private final Reference reference;
    private final HashMap<String, HashMap<Variant, VariantStats>> variants;
    private final HashMap<String, Integer> depths;
    private final HashMap<String, Integer> qualityDepths;

    /**
     * @param reference Reference to which the variants are found.
     */
    public DefaultVariantsManager(Reference reference){
        this.reference = reference;
        depths = new HashMap<>();
        qualityDepths = new HashMap<>();
        variants = new HashMap<>();
        Arrays.stream(reference.getChromosomes()).forEach(x -> variants.put(x, new HashMap<>()));
    }


    @Override
    public synchronized void reportVariant(Variant variant,
                              boolean forwardCovered,
                              boolean forward,
                              boolean reverseCovered,
                              boolean reverse) {
        VariantStats variantStat = variants.get(variant.chrom).get(variant);
        if (variantStat==null){
            variantStat = new VariantStats(variant);
            variants.get(variant.chrom).put(variant, variantStat);
        }
        variantStat.addStats(forwardCovered,
                            forward,
                            reverseCovered,
                            reverse);
    }


    @Override
    public synchronized void reportQualityCoverage(String chrom, int pos) {
        String key = location2String(chrom, pos);
        qualityDepths.putIfAbsent(key, 0);
        qualityDepths.put(key, qualityDepths.get(key)+1);
    }


    @Override
    public synchronized void reportCoverage(String chrom, int pos) {
        String key = location2String(chrom, pos);
        depths.putIfAbsent(key, 0);
        depths.put(key, depths.get(key)+1);
    }


    @Override
    public int getCoverage(String chrom, int pos) {
        Integer ret = depths.get(location2String(chrom, pos));
        return ret==null ? 0 : ret;
    }

    @Override
    public int getQualityCoverage(String chrom, int pos) {
        Integer ret = qualityDepths.get(location2String(chrom, pos));
        return ret==null ? 0 : ret;
    }

    @Override
    public Iterable<VariantStats> output() {
        return variants.keySet().stream().sorted(Comparator.comparingInt(reference::getChromIndex))
                .flatMap(key -> variants.get(key).values().stream().sorted(Comparator.comparingInt(x -> x.variant.pos)))
                .collect(Collectors.toList());
    }
}
