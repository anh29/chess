package com.example.chessengine.utility;

import com.example.chessengine.chessProcessing.HistoricInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackUpChessGame {
    private String id;
    //    public List<String> players;
    public long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    public ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<>();
    public boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;

    public final int MAX_PLAYERS = 2;
    public int counter = 0;

    public int SEARCHING_DEPTH = 4;
    private boolean isSuccess;

    public void copyFromChessGame(ChessGame chessGame) {
        if (chessGame != null) {
            this.id = chessGame.getId();
            this.WP = chessGame.getWP();
            this.WN = chessGame.getWN();
            this.WB = chessGame.getWB();
            this.WR = chessGame.getWR();
            this.WQ = chessGame.getWQ();
            this.WK = chessGame.getWK();
            this.BP = chessGame.getBP();
            this.BN = chessGame.getBN();
            this.BB = chessGame.getBB();
            this.BR = chessGame.getBR();
            this.BQ = chessGame.getBQ();
            this.BK = chessGame.getBK();
            this.EP = chessGame.getEP();
            this.HISTORIC_BITBOARD = new ArrayList<>(chessGame.getHISTORIC_BITBOARD());
            this.CWK = chessGame.isCWK();
            this.CWQ = chessGame.isCWQ();
            this.CBK = chessGame.isCBK();
            this.CBQ = chessGame.isCBQ();
            this.WhiteToMove = chessGame.isWhiteToMove();
        }
    }
}
