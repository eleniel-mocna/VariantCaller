package cz.cuni.mff.soukups3.VariantCaller;

import java.io.Serializable;
import java.util.Objects;

public record Read(String qname, int flag, String rname, int pos, int mapq, Cigar cigar,
       String rnext, int pnext, int tlen, String seq, String qual) implements Serializable {

    /**
     * Check if the given read is of the same name as this one
     * @param obj Object for comparison
     * @return Do these two reads have the same name?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Read) obj;
        return Objects.equals(this.qname, that.qname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qname);
    }

    @Override
    public String toString() {
        return "Read[" +
                "qname=" + qname + ", " +
                "flag=" + flag + ", " +
                "rname=" + rname + ", " +
                "pos=" + pos + ", " +
                "mapq=" + mapq + ", " +
                "cigar=" + cigar + ", " +
                "rnext=" + rnext + ", " +
                "pnext=" + pnext + ", " +
                "tlen=" + tlen + ", " +
                "seq=" + seq + ", " +
                "qual=" + qual + ']';
    }

    /**
     * Get the cigar at the given readIndex
     * @param readIndex At which index of the read
     * @return The cigar character for the read
     */
    public char cigarAt(int readIndex){
        return cigar.charAt(readIndex);
    }

    /**
     * Get the sequence base at the given readIndex
     * @param readIndex At which index of the read
     * @return The base character in the read
     */
    public char seqAt(int readIndex){
        if (seq.length()>readIndex) {
            return seq.charAt(readIndex);
        }
        return '\0';
    }

    /**
     * Get quality at readIndex
     * @param readIndex the given index
     * @return quality at the given readIndex
     */
    public int qualAt(int readIndex){
        if (qual.length()>readIndex) {
            return fred2int(qual.charAt(readIndex));
        }
        return 0;
    }

    /**
     * Get the length of the read from the reference's perspective.
     * @return reference observed length
     */
    public int size(){
        return tlen;
    }
    public boolean coversPosition(String chrom, int pos){
        return this.rname.equals(chrom)
                && pos >= this.pos
                && pos < this.pos + tlen;
    }
    public static int fred2int(char qual){
        return (int)qual - 33;
    }
    public static boolean isReverse(Read read){
        return (read.flag/128)%2==1;
    }

    public static boolean isForward(Read read){
        return (read.flag/64)%2==1;
    }

    public static boolean isWierd(Read read){
        return (read.flag/256)%2==1 || (read.flag/2048)%2==1;
    }
}
