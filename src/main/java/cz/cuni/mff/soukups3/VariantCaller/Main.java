package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        System.err.println("Started");
        SamReader samReader = new SamReader(new BufferedReader(new InputStreamReader(System.in)));
        Reference reference = new Reference(new ReferenceBuilder(new BufferedReader(new FileReader("src/main/resources/ref.fa"))));
        VariantsManager manager = new DefaultVariantsManager(reference);
        Core core = new Core(reference,samReader, manager, 0, 0);
        core.compute();
        System.out.print(new TSVWriter(reference).writeVariants(manager));
    }
}
