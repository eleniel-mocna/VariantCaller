package cz.cuni.mff.soukups3.VariantCaller;

import java.io.OutputStream;
import java.io.PrintStream;

public class TSVWriter implements ReportsWriter {
    private final static String header = "CHROM\tPOS\tREF\tALT\tADFP\tADFU\tADRP\tADRU\tADP\tDP\tQDP\n";
    private final static String SEP="\t";
    private Reference reference;

    public TSVWriter(Reference reference){

        this.reference = reference;
    }
     @Override
    public String writeVariants(VariantsManager manager) {
        Iterable<VariantStats> allVariants = manager.output();
        StringBuilder ret = new StringBuilder();
         ret.append(header);

        for (VariantStats variant :
                allVariants) {
            System.err.println(variant);
            ret.append(variant.variant.chrom + SEP); //CHR
            ret.append(variant.variant.pos + SEP); //POS
            // REF
            if (variant.variant.type.equals(Variant.VariantType.MISMATCH)
                || variant.variant.type.equals(Variant.VariantType.INSERTION)){
                ret.append(reference.getBase(variant.variant.chrom, variant.variant.pos) + SEP);
            }
            else {
                ret.append(reference.getBases(variant.variant.chrom,
                                            variant.variant.pos-1,
                        variant.variant.pos + variant.variant.variantString.length()) + SEP);
            }

            //ALT
            if (variant.variant.type.equals(Variant.VariantType.MISMATCH)
                || variant.variant.type.equals(Variant.VariantType.INSERTION)){
                ret.append(variant.variant.variantString + SEP);
            }
            else {
                ret.append(reference.getBase(variant.variant.chrom, variant.variant.pos-1) + SEP);
            }
            ret.append(variant.forwardPaired + SEP); //ADFP
            ret.append(variant.forwardUnpaired + SEP); //ADFU
            ret.append(variant.reversePaired + SEP); //ADRP
            ret.append(variant.reverseUnpaired + SEP); //ADRU
            ret.append(variant.paired + SEP); //ADP
            ret.append(manager.getCoverage(variant.variant.chrom, variant.variant.pos) + SEP); //DP
            ret.append(manager.getQualityCoverage(variant.variant.chrom, variant.variant.pos) + SEP); //QDP
            ret.append(System.lineSeparator());
            }

         return ret.toString();
    }
}
