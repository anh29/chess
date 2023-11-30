package com.example.chessengine.rest;

import com.example.chessengine.chessProcessing.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/chess")
public class ChessGameController {
    private final ChessMoveValidator chessMoveValidator;
    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    public static ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<HistoricInfo>();
    public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
    public static int SearchingDepth = 4;

    public ChessGameController(ChessMoveValidator chessMoveValidator) {
        this.chessMoveValidator = chessMoveValidator;
    }

    @PostMapping("/move")
    public ResponseEntity<MoveResponse> makeMove(@RequestBody MoveRequest moveRequest)
    {
        boolean isValid = chessMoveValidator.isValidMove(moveRequest, WhiteToMove);
        MoveResponse moveResponse = MoveResponse.builder().validMove(isValid).build();
        return ResponseEntity.ok(moveResponse);
    }

    @PostMapping("/fen")
    public ResponseEntity<FENResponse> parseFEN(@RequestBody FENRequest fenRequest) {
        BoardGeneration.initFromFEN(fenRequest.getFenRequest());
        FENResponse fenResponse = FENResponse.builder().build();
        return ResponseEntity.ok(fenResponse);
    }

    @PostMapping("/process")
    public ResponseEntity<MoveResponse> moveProcessing(@RequestBody MoveRequest moveRequest)
    {
//        System.out.println(moveRequest.getMove());
        Moves.moveOnBoard(moveRequest.getMove(), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        System.out.println("WP in process: " + WP);
//        boolean isValid = chessMoveValidator.isValidMove(moveRequest, WhiteToMove);
//        WhiteToMove = !WhiteToMove;
        MoveResponse moveResponse = MoveResponse.builder().validMove(true).build();
        return ResponseEntity.ok(moveResponse);
    }

    @PostMapping("/processBot")
    public ResponseEntity<MoveBotResponse> moveBotProcessing(@RequestBody MoveRequest moveRequest)
    {
        Moves.moveOnBoard(moveRequest.getMove(), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
        Searching.Negamax2(SearchingDepth, -99999999, 99999999);
        String moveBot = Searching.bestMove;
        Moves.moveOnBoard(moveBot, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
        MoveBotResponse moveBotResponse = MoveBotResponse.builder().moveBot(moveBot).isWhite(!WhiteToMove).build();
        return ResponseEntity.ok(moveBotResponse);
    }
}
