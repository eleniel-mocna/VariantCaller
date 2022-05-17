package cz.cuni.mff.soukups3.VariantCaller;

/**
 * Class which provides a String representation of found variants.
 * @param reference Reference to which the variants where found.
 */
public record TSVWriter(Reference reference) implements ReportsWriter {
    private final static String SEP = "\t";
    private final static String header = "CHROM"+SEP
            +"POS"+SEP
            +"REF"+SEP
            +"ALT"+SEP
            +"ADFP"+SEP
            +"ADFU"+SEP
            +"ADRP"+SEP
            +"ADRU"+SEP
            +"ADP"+SEP
            +"DP"+SEP
            +"QDP"+System.lineSeparator();

    @Override
    public String writeVariants(VariantsManager manager) {
        Iterable<VariantStats> allVariants = manager.output();
        StringBuilder ret = new StringBuilder();
        ret.append(header);

        for (VariantStats variant :
                allVariants) {
            ret.append(variant.variant.chrom).append(SEP); //CHR
            ret.append(variant.variant.pos).append(SEP); //POS
            // REF
            boolean notDeletion = variant.variant.type.equals(Variant.VariantType.MISMATCH)
                    || variant.variant.type.equals(Variant.VariantType.INSERTION);
            if (notDeletion) {
                ret.append(reference.getBase(variant.variant.chrom, variant.variant.pos)).append(SEP);
            } else {
                ret.append(reference.getBases(variant.variant.chrom,
                        variant.variant.pos,
                        variant.variant.pos + variant.variant.variantString.length() + 1)).append(SEP);
            }

            //ALT
            if (notDeletion) {
                ret.append(variant.variant.variantString).append(SEP);
            } else {
                ret.append(reference.getBase(variant.variant.chrom, variant.variant.pos)).append(SEP);
            }
            ret.append(variant.forwardPaired).append(SEP); //ADFP
            ret.append(variant.forwardUnpaired).append(SEP); //ADFU
            ret.append(variant.reversePaired).append(SEP); //ADRP
            ret.append(variant.reverseUnpaired).append(SEP); //ADRU
            ret.append(variant.paired).append(SEP); //ADP
            ret.append(manager.getCoverage(variant.variant.chrom, variant.variant.pos)).append(SEP); //DP
            ret.append(manager.getQualityCoverage(variant.variant.chrom, variant.variant.pos)); //QDP
            ret.append(System.lineSeparator());
        }

        return ret.toString();
    }
}
