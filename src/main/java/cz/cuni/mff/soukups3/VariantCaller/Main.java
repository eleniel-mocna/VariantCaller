package cz.cuni.mff.soukups3.VariantCaller;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        SamReader samReader = new SamReader(new BufferedReader(new InputStreamReader(System.in)));
        Thread.sleep(100);
        while (samReader.hasNext()) {
            Read[] reads = samReader.next();
            Arrays.stream(reads).forEach(System.err::println);
        }
        new Reference(new ReferenceBuilder(new BufferedReader(new FileReader("src/main/resources/ref.fa"))));
    }
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Read[] reads;
//        PipedOutputStream os = new PipedOutputStream();
//        PipedInputStream is = new PipedInputStream();
//        is.connect(os);
//        BufferedInputStream bis = new BufferedInputStream(is);
//        BufferedOutputStream bos = new BufferedOutputStream(os);
//        new Thread(() -> {
//            try {
//                read(bis);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        for (int i = 0; i < 10; i++) {
//            System.out.println("Sending string " + i + "...");
//            StringBuffer a = new StringBuffer().append("Ahoj světe!: " + i);
//            ObjectOutputStream oos = (new ObjectOutputStream(bos));
//            oos.writeObject(a);
//            System.out.println("String " + i + " sent.");
//            bos.flush();
//        }
//        System.out.println("Sending done!");
//    }
//    public static void read(BufferedInputStream bis) throws IOException, ClassNotFoundException {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("Reading " + i + "...");
//            System.out.println(new ObjectInputStream(bis).readObject());
//            System.out.println(i + "read.");
//        }
//
//    }
}
