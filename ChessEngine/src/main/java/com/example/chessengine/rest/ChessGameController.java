package com.example.chessengine.rest;

import com.example.chessengine.chessProcessing.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/chess")
public class ChessGameController {
    private final ChessMoveValidator chessMoveValidator;
    private final SimpMessagingTemplate messagingTemplate;
    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    public static ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<HistoricInfo>();
    public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
    public static int SearchingDepth = 4;
    public static String idMatchType;
    public static String idMatch;

    public ChessGameController(ChessMoveValidator chessMoveValidator, SimpMessagingTemplate messagingTemplate) {
        this.chessMoveValidator = chessMoveValidator;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/move")
    public ResponseEntity<MoveResponse> makeMove(@RequestBody MoveRequest moveRequest)
    {
        boolean isValid = chessMoveValidator.isValidMove(moveRequest, WhiteToMove);
        MoveResponse moveResponse = MoveResponse.builder().validMove(isValid).matchResult("").build();
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
        messagingTemplate.convertAndSend("/topic/move", new MoveRequest(moveRequest.getMove()));
        String status = "";
        if (!WhiteToMove) {
            if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).isEmpty()) {
                if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0) {
                    System.out.println("Black lose");
                    status = "1-0";
                } else {
                    System.out.println("0.5-0.5");
                    status = "0.5-0.5";
                }
            }
        } else {
            if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).isEmpty()) {
                if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                    System.out.println("White lose");
                    status = "0-1";
                } else {
                    System.out.println("StaleMate");
                    status = "0.5-0.5";
                }
            }
        }
        MoveResponse moveResponse = MoveResponse.builder().validMove(true).matchResult(status).build();
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

    @PostMapping("/idMatchType")
    public ResponseEntity<IdMatchTypeResponse> onlinePage(@RequestBody IdMatchTypeRequest idMatchTypeFromClient) {
        idMatchType = idMatchTypeFromClient.getIdMatchType();
        System.out.println("idMatchType: " + idMatchType);
        String securedId = RNG.generateSecureId();
        idMatch = securedId;
        System.out.println(securedId);
        IdMatchTypeResponse idMatchTypeResponse = IdMatchTypeResponse.builder().idMatch(securedId).build();
        return ResponseEntity.ok(idMatchTypeResponse);
    }
}
