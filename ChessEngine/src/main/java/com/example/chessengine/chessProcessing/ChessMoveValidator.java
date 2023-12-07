package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;
import com.example.chessengine.rest.MoveRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChessMoveValidator {
    public boolean isValidMove(MoveRequest moveRequest, boolean isWhite, String chessGameId) {
        String move = moveRequest.getMove();
        boolean turn = Objects.equals(moveRequest.getFlag(), "white");
        if (isWhite && turn) {
            return Moves.WhitePossibleMoves(ChessGameController.games.get(chessGameId).WP,
                    ChessGameController.games.get(chessGameId).WN,
                    ChessGameController.games.get(chessGameId).WB,
                    ChessGameController.games.get(chessGameId).WR,
                    ChessGameController.games.get(chessGameId).WQ,
                    ChessGameController.games.get(chessGameId).WK,
                    ChessGameController.games.get(chessGameId).BP,
                    ChessGameController.games.get(chessGameId).BN,
                    ChessGameController.games.get(chessGameId).BB,
                    ChessGameController.games.get(chessGameId).BR,
                    ChessGameController.games.get(chessGameId).BQ,
                    ChessGameController.games.get(chessGameId).BK,
                    ChessGameController.games.get(chessGameId).EP,
                    ChessGameController.games.get(chessGameId).CWK,
                    ChessGameController.games.get(chessGameId).CWQ,
                    ChessGameController.games.get(chessGameId).CBK,
                    ChessGameController.games.get(chessGameId).CBQ).contains(move);
        } else if (!isWhite && !turn) {
            return Moves.BlackPossibleMoves(ChessGameController.games.get(chessGameId).WP,
                    ChessGameController.games.get(chessGameId).WN,
                    ChessGameController.games.get(chessGameId).WB,
                    ChessGameController.games.get(chessGameId).WR,
                    ChessGameController.games.get(chessGameId).WQ,
                    ChessGameController.games.get(chessGameId).WK,
                    ChessGameController.games.get(chessGameId).BP,
                    ChessGameController.games.get(chessGameId).BN,
                    ChessGameController.games.get(chessGameId).BB,
                    ChessGameController.games.get(chessGameId).BR,
                    ChessGameController.games.get(chessGameId).BQ,
                    ChessGameController.games.get(chessGameId).BK,
                    ChessGameController.games.get(chessGameId).EP,
                    ChessGameController.games.get(chessGameId).CWK,
                    ChessGameController.games.get(chessGameId).CWQ,
                    ChessGameController.games.get(chessGameId).CBK,
                    ChessGameController.games.get(chessGameId).CBQ).contains(move);
        }
        return false;
    }
}
