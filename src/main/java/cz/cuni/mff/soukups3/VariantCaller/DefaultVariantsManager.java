package cz.cuni.mff.soukups3.VariantCaller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultVariantsManager implements VariantsManager{
    public String location2String(String chrom, int pos){
        return chrom+'\0'+pos;
    }
    private Reference reference;
    private HashMap<String, HashMap<Variant, VariantStats>> variants;
    private HashMap<String, Integer> depths;
    private HashMap<String, Integer> qualityDepths;
    public DefaultVariantsManager(Reference reference){
        this.reference = reference;
        depths = new HashMap<>();
        qualityDepths = new HashMap<>();
        variants = new HashMap<>();
        Arrays.stream(reference.getChromosomes()).forEach(x -> variants.put(x, new HashMap<>()));
    }

    @Override
    public void reportVariant(Variant variant,
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
    public void reportQualityCoverage(String chrom, int pos) {
        String key = location2String(chrom, pos);
        qualityDepths.putIfAbsent(key, 0);
        qualityDepths.put(key, qualityDepths.get(key)+1);
    }

    @Override
    public void reportCoverage(String chrom, int pos) {
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
        return variants.keySet().stream()
                .flatMap(key -> variants.get(key).values().stream().sorted())
                .collect(Collectors.toList());
    }
}
