package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;
import com.example.chessengine.rest.MoveRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChessMoveValidator {
    public boolean isValidMove(MoveRequest moveRequest, boolean isWhite) {
        String move = moveRequest.getMove();
        boolean turn = Objects.equals(moveRequest.getFlag(), "white");
        if (isWhite && turn) {
            return Moves.WhitePossibleMoves(ChessGameController.WP,
                    ChessGameController.WN,
                    ChessGameController.WB,
                    ChessGameController.WR,
                    ChessGameController.WQ,
                    ChessGameController.WK,
                    ChessGameController.BP,
                    ChessGameController.BN,
                    ChessGameController.BB,
                    ChessGameController.BR,
                    ChessGameController.BQ,
                    ChessGameController.BK,
                    ChessGameController.EP,
                    ChessGameController.CWK,
                    ChessGameController.CWQ,
                    ChessGameController.CBK,
                    ChessGameController.CBQ).contains(move);
        } else if (!isWhite && !turn) {
            return Moves.BlackPossibleMoves(ChessGameController.WP,
                    ChessGameController.WN,
                    ChessGameController.WB,
                    ChessGameController.WR,
                    ChessGameController.WQ,
                    ChessGameController.WK,
                    ChessGameController.BP,
                    ChessGameController.BN,
                    ChessGameController.BB,
                    ChessGameController.BR,
                    ChessGameController.BQ,
                    ChessGameController.BK,
                    ChessGameController.EP,
                    ChessGameController.CWK,
                    ChessGameController.CWQ,
                    ChessGameController.CBK,
                    ChessGameController.CBQ).contains(move);
        }
        return false;
    }
}
