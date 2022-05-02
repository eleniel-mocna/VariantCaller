package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class SamReader implements ReadsProvider, Iterator<Read[]> {
    private final BufferedReader inputReader;
    private final int BUFFER_CAPACITY=100000;
    private final Thread supplyThread;
    private Read lastRead;
    private final ArrayBlockingQueue<Read[]> outputBuffer = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    private boolean ended = false;
    public SamReader(BufferedReader is) {
        this.inputReader = is;
        supplyThread = new Thread(() -> {
            try {
                start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        supplyThread.start();
    }

    public void start() throws IOException {
        lastRead=getNextRead();
        Read[] newReads;
        while ((newReads=getNextReads())!=null){
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
                    return (new Read(lineSeparated[0],
                            Integer.parseInt(lineSeparated[1]),
                            lineSeparated[2],
                            Integer.parseInt(lineSeparated[3]),
                            Integer.parseInt(lineSeparated[4]),
                            Cigar.fromString(lineSeparated[5]),
                            lineSeparated[6],
                            Integer.parseInt(lineSeparated[7]),
                            Integer.parseInt(lineSeparated[8]),
                            lineSeparated[9],
                            lineSeparated[6]));
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
            try {
                result = outputBuffer.take();
            } catch (InterruptedException e) {
                System.err.println("SamReader queue interrupted!");
                result = null;
            }
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