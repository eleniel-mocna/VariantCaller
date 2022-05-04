package cz.cuni.mff.soukups3.VariantCaller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class Core extends RecursiveAction {
    private final Reference reference;
    private final ReadsProvider readsProvider;
    private final VariantsManager variantsManager;
    private final int minMapQ;
    private final int minQual;

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
        while (readsProvider.hasNext()){
            analyzeReads(readsProvider.getNext());
        }

    }
    private LinkedList<Variant> analyzeRead(Read read){
        if (read.mapq()<minMapQ){
            return new LinkedList<>();
        }
        LinkedList<Variant> variants = new LinkedList<>();
        Character readBase;
        char referenceBase;
        char lastCigar = 'M';
        int qual;
        int lastQual=100000;
        int refIndex=read.pos();
        int readIndex=0;
        for (char cigar :
                read.cigar()) {
            readBase=read.seqAt(readIndex);
            referenceBase = reference.getBase(read.rname(), refIndex);
            qual=read.qualAt(readIndex);
            boolean qualityPassed = qual>=minQual;
            switch (Character.toUpperCase(cigar)){
                case 'M', 'X', '=' -> {
                    if (qualityPassed){
                        variantsManager.reportQualityCoverage(read.rname(), refIndex);
                    }
                    variantsManager.reportCoverage(read.rname(), refIndex);
                    if (!readBase.equals(referenceBase)) {
                        if (qualityPassed){
                            variants.add(new Variant(Variant.VariantType.MISMATCH,
                                    read.rname(), refIndex, Character.toString(readBase)));
                        }
                    }
                    refIndex++;
                    readIndex++;
                }
                case 'I' -> {

                    if (cigar==lastCigar){
                        lastQual=Math.min(lastQual, qual);
                        if (lastQual<minQual){
                            variants.pop();
                        }
                        else {
                            //noinspection ConstantConditions //Is checked by lastCigar
                            variants.peekLast().addToVariantString(readBase);
                        }
                    }
                    else {
                        lastQual=qual;
                        if (lastQual >= minQual) {
                            variants.add(new Variant(Variant.VariantType.INSERTION,
                                                    read.rname(),
                                                    refIndex-1,
                                                    Character.toString(reference.getBase(read.rname(),refIndex-1))));
                            variants.peekLast().addToVariantString(readBase);
                        }
                    }

                    readIndex++;
                }
                case 'D' -> {
                    if (lastCigar=='D'){
                        //noinspection ConstantConditions //Presence is checked by lastCigar
                        variants.peekLast().addToVariantString('\0');
                    }
                    else {
                        variants.add(new Variant(Variant.VariantType.DELETION,
                                                read.rname(),
                                                refIndex-1,
                                                "\0"));
                    }
                    refIndex++;
                }
                case 'N' -> refIndex++;
                case 'S' -> readIndex++;
                case 'H', 'P' -> {}
                default -> throw new IllegalStateException("Unexpected value: " + cigar);
            }
            lastCigar=cigar;
        }
        return variants;
    }
    private void analyzeReads(Read[] reads){
        List<LinkedList<Variant>> variantss = Arrays.stream(reads).map(this::analyzeRead).toList();
        int remainingVariants = variantss.stream().mapToInt(LinkedList::size).sum();
        while (remainingVariants>0){
            boolean forward=false;
            boolean reverse=false;
            boolean forwardCoverage=false;
            boolean reverseCoverage=false;
            int minPos = variantss.stream().mapToInt(
                    variant -> (variant.peek()==null) ? Integer.MAX_VALUE : variant.peek().pos)
                    .min().getAsInt();
            // Now we find one of the first variants.
            @SuppressWarnings("OptionalGetWithoutIsPresent") // Is checked by remainingVariants
            Variant currentVariant = variantss.stream()
                    .map(LinkedList::peek).filter(x -> x!=null && x.pos==minPos)
                    .findAny().get();

            // variants are ordered by pos. So this is just a `merge` (as in mergesort)
            // equivalent. We pop all identical variants with the lowest pos and get stats for them.
            for (int i = 0; i < variantss.size(); i++) {
                LinkedList<Variant> variants = variantss.get(i);
                if ((variants.peek())!=null && variants.peek().equals(currentVariant)){
                    variants.pop();
                    forward = forward || Read.isForward(reads[i]);
                    reverse = reverse || Read.isReverse(reads[i]);
                }
                forwardCoverage = forwardCoverage
                        || (reads[i].coversPosition(currentVariant.chrom, currentVariant.pos)
                            && Read.isForward(reads[i]));
                reverseCoverage = reverseCoverage
                        || (reads[i].coversPosition(currentVariant.chrom, currentVariant.pos)
                        && Read.isReverse(reads[i]));
            }
            variantsManager.reportVariant(currentVariant,
                                            forwardCoverage,
                                            forward,
                                            reverseCoverage,
                                            reverse);
        remainingVariants = variantss.stream().mapToInt(LinkedList::size).sum();
        }
    }
}
