package cz.cuni.mff.soukups3.VariantCaller;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class managing one Read from the sam file.
 * @param qname Name of the read
 * @param flag Flag info from the read
 * @param rname Chromosome name
 * @param pos Position inside the chromosome (1-based)
 * @param mapq Mapping quality
 * @param cigar Cigar string
 * @param rnext Next (pair) read's chromosome
 * @param pnext Next (pair) read's position
 * @param tlen Template length (length over reference)
 * @param seq Sequence inside this read
 * @param qual Per base quality
 */
public record Read(
        String qname,
        int flag,
        String rname,
        int pos,
        int mapq,
        Cigar cigar,
        String rnext,
        int pnext,
        int tlen,
        String seq,
        String qual
) implements Serializable {

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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public int size(){
        return tlen;
    }

    /**
     * @param chrom Chromosome of the location
     * @param pos Position of the location
     * @return Does this read cover the given location
     */
    public boolean coversPosition(String chrom, int pos){
        return this.rname.equals(chrom)
                && pos >= this.pos
                && pos < this.pos + tlen;
    }

    /**
     * @param qual Character encrypted quality
     * @return Real value of this quality
     */
    public static int fred2int(char qual){
        return (int)qual - 33;
    }

    /**
     * @param read Read to be checked
     * @return Is this Read on the reverse strand
     */
    public static boolean isReverse(Read read){
        return (read.flag/128)%2==1;
    }

    /**
     * @param read Read to be checked
     * @return Is this Read on the forward strand?
     */
    public static boolean isForward(Read read){
        return (read.flag/64)%2==1;
    }


    /**
     * @param read Read to be checked
     * @return Is this read wierd (supplementary or secondary alignment)?
     */
    public static boolean isWierd(Read read){
        return (read.flag/256)%2==1 || (read.flag/2048)%2==1;
    }
}
