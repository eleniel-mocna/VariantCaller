package cz.cuni.mff.soukups3.VariantCaller;

import java.util.LinkedList;
import java.util.concurrent.RecursiveAction;

public class Core extends RecursiveAction {
    private Reference reference;
    private ReadsProvider readsProvider;
    private VariantsManager variantsManager;
    private int minMapQ;
    private int minQual;

    public Core(Reference reference,
                ReadsProvider readsProvider,
                VariantsManager variantsManager,
                int minMapQ,
                int minQual){
        this.reference = reference;
        this.readsProvider = readsProvider;
        this.variantsManager = variantsManager;
        this.minMapQ = minMapQ;
        this.minQual = minQual;
    }
    @Override
    protected void compute() {
        Read[] reads = readsProvider.getNext();

    }
    private Variant[] analyzeRead(Read read){
        if (read.mapq()<minMapQ){
            return new Variant[0];
        }
        LinkedList<Variant> variants = new LinkedList<>();
        Character readBase;
        Character referenceBase;
        int qual;
        int refIndex=read.pos();
        int readIndex=0;
        for (char cigar :
                read.cigar()) {
            readBase=read.seqAt(readIndex);
            referenceBase = reference.getBase(read.rname(), refIndex);
            cigar=read.cigarAt(readIndex);
            qual=read.qualAt(readIndex);
            if (qual<minQual){
                continue;
            }
            switch (cigar){
                case 'M' | 'X' | '=' -> {
                    if (readBase.equals(referenceBase)){
                        continue;
                    }
                    else {
                        variants.add(new Variant(read.rname(), refIndex, Character.toString(readBase)));
                    }
                    refIndex++;
                    readIndex++;
                }
                case 'I' -> {
                    readIndex++;
                }
                case 'D' -> {

                    refIndex++;
                }
                case 'N' -> {
                    refIndex++;
                }
                case 'S' -> {
                    readIndex++;
                }
                case 'H' | 'P' -> {}
                default -> throw new IllegalStateException("Unexpected value: " + cigar);
            }
        }
        return variants.toArray(new Variant[0]);
    }
}
