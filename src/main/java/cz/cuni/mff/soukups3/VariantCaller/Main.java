package cz.cuni.mff.soukups3.VariantCaller;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;


/**
 * Main class, which runs when the app is started.
 */
public class Main {
    @Option(name="--minMapQ",usage="Minimal mapping quality.")
    private final int minMapQ=0;

    @Option(name="--minBaseQ",usage="Minimal base quality.")
    private final int minBaseQ=0;

    @Option(name="--reference",usage="Path to the reference file.")
    private final String reference_path ="/reference/hg19.fa";

    @Option(name="--threads",usage="Maximum number of processing threads")
    private final int nThreads=1;

    @Option(name="--tsv",usage="Path to the output tsv file.")
    private final String tsv_path="";

    /**
     * Syntactic only main.
     * @param args Arguments passed to main
     * @throws IOException When some read from disk fails
     */
    public static void main(String[] args) throws IOException {
        new Main().doMain(args);
    }

    /**
     * Main but non-static.
     * @param args Arguments passed to main
     * @throws IOException When some read from disk fails
     */
    public void doMain(String[] args) throws IOException {
        parseArguments(args);
        System.err.println(reference_path);
        System.err.println("Started");
        long start;
        System.err.println(start=System.currentTimeMillis());
        SamReader samReader = new SamReader(new BufferedReader(new InputStreamReader(System.in)));
        Reference reference = new Reference(new ReferenceBuilder(reference_path));
        VariantsManager manager = new DefaultVariantsManager(reference);
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Math.min(nThreads, Runtime.getRuntime().availableProcessors()); i++) {
            Core core = new Core(reference,samReader, manager, minMapQ, minBaseQ);
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
        if (!"".equals(tsv_path)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tsv_path))) {
                bw.write(new TSVWriter(reference).writeVariants(manager));
            }
        } else {
        System.out.print(new TSVWriter(reference).writeVariants(manager));
        }
        System.err.println("Time elapsed: " + ((System.currentTimeMillis()-start)/1000.0) + " s");
    }

    private void parseArguments(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar VariantCaller.jar [options...]");
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("  Example: java SampleMain"+parser.printExample(ALL));
            return;
        }
    }
}
// 140.313
//  80.621