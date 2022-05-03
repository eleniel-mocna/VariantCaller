package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        System.err.println("Started");
        SamReader samReader = new SamReader(new BufferedReader(new InputStreamReader(System.in)));
        System.err.println("1");
        Reference reference = new Reference(new ReferenceBuilder(new BufferedReader(new FileReader("src/main/resources/ref.fa"))));
        System.err.println("2");
        VariantsManager manager = new DefaultVariantsManager(reference);
        System.err.println("3");
        Core core = new Core(reference,samReader, manager, 0, 0);
        System.err.println("4");
        core.compute();
        System.err.println("5");
        System.out.println(new TSVWriter(reference).writeVariants(manager));
    }
}
