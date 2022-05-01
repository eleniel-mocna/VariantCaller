package cz.cuni.mff.soukups3.VariantCaller;

import java.util.concurrent.RecursiveAction;

public class Core extends RecursiveAction {
    private ReadsProvider readsProvider;
    private VariantsManager variantsManager;

    public Core(ReadsProvider readsProvider, VariantsManager variantsManager){
        this.readsProvider = readsProvider;
        this.variantsManager = variantsManager;
    }
    @Override
    protected void compute() {
        Read[] reads = readsProvider.getNext();

    }
}
