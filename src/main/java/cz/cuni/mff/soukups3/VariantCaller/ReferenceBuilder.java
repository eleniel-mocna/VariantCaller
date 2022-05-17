package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This class takes a BufferedReader and does all the hard
 * stuff around it needed for the Reference object.
 * Reference object is then created from this class.
 */
public class ReferenceBuilder {
    private final LinkedHashMap<String, Character[]> bases = new LinkedHashMap<>();
    private ArrayList<Character> currentChromosome;
    private String currentChromosomeName = "";
    private int whichChrom=0;
    private Integer[] chromosomeLengths;

    /**
     * Prepare everything for the Reference object
     * @param path to the reference .fa(sta) file
     * @throws FileNotFoundException When the file is not found
     */
    public ReferenceBuilder(String path) throws FileNotFoundException {
        String line;
        BufferedReader reader;
        chromosomeLengths=getChromLengths(path);
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            System.err.println("Reference file not found! Exiting...");
            throw new FileNotFoundException("Reference file not found! Exiting...");
        }
        System.err.println("Building reference:");
        try {
            while ((line = reader.readLine()) != null) {
                if ("@;<>L".contains(line.substring(0,1))){
                    processHeader(line.substring(1));
                }
                else {
                    processSequenceLine(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Reference reader failed on" + e);
        }
        if (currentChromosome.size()>1){
            System.err.println("Read chrom " + currentChromosomeName + ", length:" + currentChromosome.size());
            bases.put(currentChromosomeName, currentChromosome.toArray(new Character[0]));
        }
        saveChromLengths(path);
    }
    private Integer[] getChromLengths(String faPath){
        String newPath = faPath+".lengths";
        ArrayList<Integer> ret = new ArrayList<>();
        File file = new File(newPath);
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((line=br.readLine())!=null){
                if (line.strip().length()>0) {
                    ret.add(Integer.parseInt(line));
                }
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            ret = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret.toArray(new Integer[0]);
    }

    private void saveChromLengths(String faPath){
        chromosomeLengths = bases.values().stream().mapToInt(x->x.length).boxed().toArray(Integer[]::new);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(faPath+".lengths"))){
            bases.values().stream().mapToInt(x->x.length).forEach(x->{
                try {
                    bw.write(""+x);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.err.println("Chromosome lengths not written:");
            e.printStackTrace();
        }

    }

    private int getNextChromLength(){
        if (whichChrom>=chromosomeLengths.length){
            return 0;
        }
        else return chromosomeLengths[whichChrom++];
    }
    private void processSequenceLine(String line) {
        currentChromosome.addAll(line.chars().mapToObj(x -> (char) x).map(Character::toUpperCase).toList());
    }

    private void processHeader(String line) {
        if (currentChromosome==null) {
            int l = getNextChromLength()+1;
            System.err.println("Next length: " + l);
            currentChromosome=new ArrayList<>(l);
            currentChromosome.add('\0');
        }
        else if (currentChromosome.size()>1){
            System.err.println("Read chrom " + currentChromosomeName + ", length:" + currentChromosome.size());
            bases.put(currentChromosomeName, currentChromosome.toArray(new Character[0]));
            int l = getNextChromLength()+1;
            System.err.println("Next length: " + l);
            currentChromosome=new ArrayList<>(l);
            currentChromosome.add('\0');
        }
        currentChromosomeName=line;

    }

    /**
     * @return The bases built by this builder
     */
    public LinkedHashMap<String, Character[]> getBases() {
        return bases;
    }
}
