package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws IOException {
        System.err.println("Started");

        long start;
        System.err.println(start=System.currentTimeMillis());
        SamReader samReader = new SamReader(new BufferedReader(new InputStreamReader(System.in)));
        Reference reference = new Reference(new ReferenceBuilder(args[0]));
        VariantsManager manager = new DefaultVariantsManager(reference);
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Core core = new Core(reference,samReader, manager, 0, 0);
            Thread thread = new Thread(core::compute);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread :
                threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("out.tsv"))){
            bw.write(new TSVWriter(reference).writeVariants(manager));
        }
//        System.out.print(new TSVWriter(reference).writeVariants(manager));
        System.err.println("Time elapsed: " + ((System.currentTimeMillis()-start)/1000.0) + " s");
    }
}
// 140.313
//  80.621