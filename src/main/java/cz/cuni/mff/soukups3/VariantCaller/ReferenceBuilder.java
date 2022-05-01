package cz.cuni.mff.soukups3.VariantCaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * This class takes a BufferedReader and does all the hard
 * stuff around it needed for the Reference object.
 * Reference object is then created from this class.
 */
public class ReferenceBuilder {
    // TODO: Add wrapper around these so that Arrays of size Long are also possible.
    private HashMap<String, Character[]> bases = new HashMap<>();
    private LinkedList<Character> currentChromosome;
    private String currentChromosomeName = "";
    /**
     * @param reader Reader with the fasta reference file in it.
     */
    public ReferenceBuilder(BufferedReader reader) {
        String line;
        currentChromosome = new LinkedList<>();
        currentChromosome.add('\0');
        try {
            while ((line = reader.readLine()) != null) {
                if ("@;<L".contains(line.substring(0,1))){
                    processHeader(line.substring(1));
                }
                else {
                    processSequenceLine(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Reference reader failed on" + e);
        }
    }

    private void processSequenceLine(String line) {
        currentChromosome.addAll(line.chars().mapToObj(x -> (char)x).collect(Collectors.toList()));
    }

    private void processHeader(String line) {
        if (currentChromosome.size()>1){
            bases.put(currentChromosomeName, currentChromosome.toArray(new Character[0]));
        }
        currentChromosomeName=line;
        currentChromosome=new LinkedList<>();
        currentChromosome.add('\0');
    }

    public HashMap<String, Character[]> getBases() {
        return bases;
    }
}
