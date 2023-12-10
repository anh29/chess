package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;
import com.example.chessengine.rest.MoveRequest;
import com.example.chessengine.utility.MatchCombinedId;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChessMoveValidator {
    public boolean isValidMove(MoveRequest moveRequest, boolean isWhite, String idMatchType, String chessGameId) {
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idMatchType).matchId(chessGameId).build();
        String move = moveRequest.getMove();
        boolean turn = Objects.equals(moveRequest.getFlag(), "white");
        if (isWhite && turn) {
            return Moves.WhitePossibleMoves(ChessGameController.games.get(combinedId).WP,
                    ChessGameController.games.get(combinedId).WN,
                    ChessGameController.games.get(combinedId).WB,
                    ChessGameController.games.get(combinedId).WR,
                    ChessGameController.games.get(combinedId).WQ,
                    ChessGameController.games.get(combinedId).WK,
                    ChessGameController.games.get(combinedId).BP,
                    ChessGameController.games.get(combinedId).BN,
                    ChessGameController.games.get(combinedId).BB,
                    ChessGameController.games.get(combinedId).BR,
                    ChessGameController.games.get(combinedId).BQ,
                    ChessGameController.games.get(combinedId).BK,
                    ChessGameController.games.get(combinedId).EP,
                    ChessGameController.games.get(combinedId).CWK,
                    ChessGameController.games.get(combinedId).CWQ,
                    ChessGameController.games.get(combinedId).CBK,
                    ChessGameController.games.get(combinedId).CBQ).contains(move);
        } else if (!isWhite && !turn) {
            return Moves.BlackPossibleMoves(ChessGameController.games.get(combinedId).WP,
                    ChessGameController.games.get(combinedId).WN,
                    ChessGameController.games.get(combinedId).WB,
                    ChessGameController.games.get(combinedId).WR,
                    ChessGameController.games.get(combinedId).WQ,
                    ChessGameController.games.get(combinedId).WK,
                    ChessGameController.games.get(combinedId).BP,
                    ChessGameController.games.get(combinedId).BN,
                    ChessGameController.games.get(combinedId).BB,
                    ChessGameController.games.get(combinedId).BR,
                    ChessGameController.games.get(combinedId).BQ,
                    ChessGameController.games.get(combinedId).BK,
                    ChessGameController.games.get(combinedId).EP,
                    ChessGameController.games.get(combinedId).CWK,
                    ChessGameController.games.get(combinedId).CWQ,
                    ChessGameController.games.get(combinedId).CBK,
                    ChessGameController.games.get(combinedId).CBQ).contains(move);
        }
        return false;
    }
}
