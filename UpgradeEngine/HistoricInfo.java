package UpgradeEngine;

public class HistoricInfo {
    public long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    public boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;

    public HistoricInfo(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove) {
        this.WP = WP;
        this.WN = WN;
        this.WB = WB;
        this.WR = WR;
        this.WQ = WQ;
        this.WK = WK;
        this.BP = BP;
        this.BN = BN;
        this.BB = BB;
        this.BR = BR;
        this.BQ = BQ;
        this.BK = BK;
        this.EP = EP;
        this.CWK = CWK;
        this.CWQ = CWQ;
        this.CBK = CBK;
        this.CBQ = CBQ;
        this.WhiteToMove = WhiteToMove;
    }
}
