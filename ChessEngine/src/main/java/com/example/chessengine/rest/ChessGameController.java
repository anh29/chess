package com.example.chessengine.rest;

import com.example.chessengine.chessProcessing.*;
import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/chess")
public class ChessGameController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountMatchService accountMatchService;

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
        String matchScore = "";
        if (!WhiteToMove) {
            if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).isEmpty()) {
                if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0) {
                    System.out.println("Black lose");
                    matchScore = "1-0";
                } else {
                    System.out.println("0.5-0.5");
                    matchScore = "0.5-0.5";
                }
            }
        } else {
            if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).isEmpty()) {
                if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                    System.out.println("White lose");
                    matchScore = "0-1";
                } else {
                    System.out.println("StaleMate");
                    matchScore = "0.5-0.5";
                }
            }
        }
        if (!matchScore.isEmpty()) {
            Matches match = matchService.getMatchByIdMatch(idMatch);
            match.setScore(matchScore);
            match.setMoves(moveRequest.getAllMoves());
            match.setStatus(2);
            matchService.save(match);
        }
        MoveResponse simpTempResponse = MoveResponse.builder().move(moveRequest.getMove()).matchResult(matchScore).build();
        messagingTemplate.convertAndSend("/topic/move", simpTempResponse);
        MoveResponse moveResponse = MoveResponse.builder().validMove(true).matchResult(matchScore).build();
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
        List<Matches> matchesList = matchService.getAllMatchesByStatus(0);
        if (matchesList.isEmpty()) {
            String securedId = RNG.generateSecureId();
            idMatch = securedId;
            System.out.println(securedId);
            matchService.save(Matches.builder().matchId(idMatch).status(0).build());
        } else {
            idMatch = matchesList.get(0).getMatchId();
            Matches match = matchService.getMatchByIdMatch(idMatch);
            match.setStatus(1);
            matchService.save(match);
            System.out.println("idMatchhhhhhhh: " + idMatch);
        }
        IdMatchTypeResponse idMatchTypeResponse = IdMatchTypeResponse.builder().idMatch(idMatch).build();
        return ResponseEntity.ok(idMatchTypeResponse);
    }

    @PostMapping("/endgame")
    public ResponseEntity<Void> endgameHandling(@RequestBody MoveRequest moveRequest) {
        matchService.updateMatchMoves(idMatch, moveRequest.getAllMoves());
        return ResponseEntity.ok(null);
    }
}
