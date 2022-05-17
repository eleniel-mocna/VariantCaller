package cz.cuni.mff.soukups3.VariantCaller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Record managing cigar string for a read
 * @param lengths Lengths of individual cigar characters (how many times should it be applied)
 * @param values Values of cigar characters
 */
public record Cigar(Integer[] lengths, Character[] values) implements Iterable<Character> {
    /**
     * @param cigar String from a sam file to be used as a cigar
     * @return Cigar object created from the given cigar string
     */
    public static Cigar fromString(String cigar){
        LinkedList<Integer> lengths = new LinkedList<>();
        LinkedList<Character> values = new LinkedList<>();
        StringBuilder numberBuilder =new StringBuilder();
        for (int c:
                cigar.chars().toArray() ) {
            if (Character.isAlphabetic(c)){
                lengths.add(Integer.parseInt(String.valueOf(numberBuilder)));
                numberBuilder=new StringBuilder();
                values.add((char) c);
            }
            else {
                numberBuilder.append((char) c);
            }
        }
        return new Cigar(lengths.toArray(new Integer[0]), values.toArray(new Character[0]));
    }
    public String toString(){
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < lengths.length; i++) {
            ret.append(lengths[i]);
            ret.append(values[i]);
        }
        return ret.toString();
    }

    /**
     * @return Number of cigar values (1 value per base)
     */
    public int size(){
        return Arrays.stream(lengths).mapToInt(x -> x).sum();
    }

    /**
     * @param index Index in the corresponding read
     * @return Corresponding cigar at the given index
     */
    public char charAt(int index){
        if (index>=size() || index < 0){
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < lengths.length; i++) {
            if (index<lengths[i]) return values[i];
            index-=lengths[i];
        }
        throw new IndexOutOfBoundsException("This should never be thrown!!!");
    }

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public Character next() {
                return charAt(index++);
            }
        };
    }
}
