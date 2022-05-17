package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class which takes Reads from a Sam file from a reader and provides them via ReadProvider.
 */
public class SamReader implements ReadsProvider, Iterator<Read[]>, Runnable {
    private final BufferedReader inputReader;
    @SuppressWarnings("FieldCanBeLocal")
    private final int BUFFER_CAPACITY=100000;
    @SuppressWarnings("FieldCanBeLocal")
    private final Thread supplyThread;
    private Read lastRead;
    private final LinkedList<Read[]> outputBuffer = new LinkedList<>();
    private boolean ended = false;

    /**
     * @param bufferedReader Reader from which the Reads are read.
     */
    public SamReader(BufferedReader bufferedReader) {
        System.err.println("SamReader Started!");
        this.inputReader = bufferedReader;
        supplyThread = new Thread(this);
        supplyThread.start();
    }

    public void run() {
        try {
            lastRead=getNextRead();
        } catch (IOException e) {
            System.err.println("SamReader failed on:" + e);
            return;
        }
        Read[] newReads;
        System.err.println(outputBuffer.size());
        while (true){
            try {
                if ((newReads = getNextReads()) == null) break;
            } catch (IOException e) {
                System.err.println("SamReader failed on:" + e);
                return;
            }
            while (outputBuffer.size()>=BUFFER_CAPACITY){
                try {
                    //noinspection BusyWait // I will keep it this way, it's easier and shouldn't make a big difference
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
            outputBuffer.add(newReads);
        }
    }

    /**
     * Get an array of Reads of the same name, or null if none are left.
     * @return Reads of which all have the same name.
     * @throws IOException When Reader fails.
     */
    public Read[] getNextReads() throws IOException {
        if (lastRead==null){
            ended=true;
            return null;
        }
        Read newRead;
        LinkedList<Read> retList = new LinkedList<>();
        while (true){
            retList.add(lastRead);
            newRead = getNextRead();
            if ((newRead == null) || !newRead.qname().equals(lastRead.qname())){
                lastRead=newRead;
                break;
            }
            lastRead=newRead;
        }
        return retList.toArray(new Read[0]);
    }

    /**
     * Get one next Read.
     * @return Next Read or null if EOF was reached
     * @throws IOException When inputReader fails
     */
    private Read getNextRead() throws IOException {

        String line;
        String[] lineSeparated;
        while ((line = inputReader.readLine()) != null) {
            if (line.charAt(0) != '@') {
                lineSeparated = line.split("\t");
                try {
                    Read ret = new Read(lineSeparated[0],
                            Integer.parseInt(lineSeparated[1]),
                            lineSeparated[2],
                            Integer.parseInt(lineSeparated[3]),
                            Integer.parseInt(lineSeparated[4]),
                            Cigar.fromString(lineSeparated[5]),
                            lineSeparated[6],
                            Integer.parseInt(lineSeparated[7]),
                            Integer.parseInt(lineSeparated[8]),
                            lineSeparated[9],
                            lineSeparated[10]);
                    if (!Read.isWierd(ret)) {
                        return ret;
                    }

                } catch (IndexOutOfBoundsException e){
                    System.err.println("SamReader: Ignoring line: \"" + line + "\"");
                }
            }
        }
        return null;
    }

    /**
     * @return Returns an array of Reads of the same name or null if nothing is available (end or interrupt)
     */
    @Override
    public synchronized Read[] getNext() {
        Read[] result;
        if (hasNext()) {
            while (hasNext() && outputBuffer.size()==0){
                try {
                    //noinspection BusyWait // I will keep it this way, it's easier and shouldn't make a big difference
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
            result = outputBuffer.pollFirst();
        }
        else {
            result=null;
        }
        return result;
    }

    @Override
    public synchronized boolean hasNext() {
        return !(ended && outputBuffer.isEmpty());
    }

    @Override
    public Read[] next() {
        return getNext();
    }
}