package cz.cuni.mff.soukups3.VariantCaller;

import java.io.Serializable;
import java.util.Objects;

public record Read(String qname, int flag, String rname, int pos, int mapq, String cigar,
       String rnext, int pnext, int tlen, String seq, String qual) implements Serializable {

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
}
