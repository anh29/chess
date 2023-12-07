package com.example.chessengine.rest;

import com.example.chessengine.chessProcessing.BoardGeneration;
import com.example.chessengine.chessProcessing.ChessMoveValidator;
import com.example.chessengine.chessProcessing.Moves;
import com.example.chessengine.constant.MatchStatus;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.MatchService;
import com.example.chessengine.utility.ChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/chess")
public class ChessGameController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountMatchService accountMatchService;

    public static ConcurrentHashMap<String, ChessGame> games = new ConcurrentHashMap<>();

    private final ChessMoveValidator chessMoveValidator;
    private final SimpMessagingTemplate messagingTemplate;
//    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
//    public static ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<HistoricInfo>();
//    public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
    public static int SearchingDepth = 4;
    public static String idMatchType;
    public static String idMatch;

    public ChessGameController(ChessMoveValidator chessMoveValidator, SimpMessagingTemplate messagingTemplate) {
        this.chessMoveValidator = chessMoveValidator;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/move/{matchId}")
    public ResponseEntity<MoveResponse> makeMove(@PathVariable String matchId, @RequestBody MoveRequest moveRequest)
    {
        boolean isValid = chessMoveValidator.isValidMove(moveRequest, games.get(matchId).WhiteToMove, matchId);
        MoveResponse moveResponse = MoveResponse.builder().validMove(isValid).matchResult("").build();
        return ResponseEntity.ok(moveResponse);
    }

    @PostMapping("/fen/{matchId}")
    public ResponseEntity<FENResponse> parseFEN(@PathVariable String matchId, @RequestBody FENRequest fenRequest) {
        BoardGeneration.initFromFEN(fenRequest.getFenRequest(), matchId);
        FENResponse fenResponse = FENResponse.builder().build();
        return ResponseEntity.ok(fenResponse);
    }

    @PostMapping("/process/{matchId}")
    public ResponseEntity<MoveResponse> moveProcessing(@PathVariable String matchId, @RequestBody MoveRequest moveRequest)
    {
//        System.out.println(moveRequest.getMove());
        Moves.moveOnBoard(moveRequest.getMove(), games.get(matchId).WP, games.get(matchId).WN, games.get(matchId).WB, games.get(matchId).WR, games.get(matchId).WQ, games.get(matchId).WK, games.get(matchId).BP, games.get(matchId).BN, games.get(matchId).BB, games.get(matchId).BR, games.get(matchId).BQ, games.get(matchId).BK, games.get(matchId).EP, games.get(matchId).CWK, games.get(matchId).CWQ, games.get(matchId).CBK, games.get(matchId).CBQ, games.get(matchId).WhiteToMove, matchId);
//        System.out.println("WP in process: " + WP);
//        boolean isValid = chessMoveValidator.isValidMove(moveRequest, WhiteToMove);
//        WhiteToMove = !WhiteToMove;
        String matchScore = "";
        if (!games.get(matchId).WhiteToMove) {
            if (Moves.BlackPossibleMoves(games.get(matchId).WP, games.get(matchId).WN, games.get(matchId).WB, games.get(matchId).WR, games.get(matchId).WQ, games.get(matchId).WK, games.get(matchId).BP, games.get(matchId).BN, games.get(matchId).BB, games.get(matchId).BR, games.get(matchId).BQ, games.get(matchId).BK, games.get(matchId).EP, games.get(matchId).CWK, games.get(matchId).CWQ, games.get(matchId).CBK, games.get(matchId).CBQ).isEmpty()) {
                if ((Moves.unsafeForBlack(games.get(matchId).WP, games.get(matchId).WN, games.get(matchId).WB, games.get(matchId).WR, games.get(matchId).WQ, games.get(matchId).WK, games.get(matchId).BP, games.get(matchId).BN, games.get(matchId).BB, games.get(matchId).BR, games.get(matchId).BQ, games.get(matchId).BK) & games.get(matchId).BK) != 0) {
                    System.out.println("Black lose");
                    matchScore = "1-0";
                } else {
                    System.out.println("0.5-0.5");
                    matchScore = "0.5-0.5";
                }
            }
        } else {
            if (Moves.WhitePossibleMoves(games.get(matchId).WP, games.get(matchId).WN, games.get(matchId).WB, games.get(matchId).WR, games.get(matchId).WQ, games.get(matchId).WK, games.get(matchId).BP, games.get(matchId).BN, games.get(matchId).BB, games.get(matchId).BR, games.get(matchId).BQ, games.get(matchId).BK, games.get(matchId).EP, games.get(matchId).CWK, games.get(matchId).CWQ, games.get(matchId).CBK, games.get(matchId).CBQ).isEmpty()) {
                if ((Moves.unsafeForWhite(games.get(matchId).WP, games.get(matchId).WN, games.get(matchId).WB, games.get(matchId).WR, games.get(matchId).WQ, games.get(matchId).WK, games.get(matchId).BP, games.get(matchId).BN, games.get(matchId).BB, games.get(matchId).BR, games.get(matchId).BQ, games.get(matchId).BK) & games.get(matchId).WK) != 0) {
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
            match.setStatus(MatchStatus.ENDED);
            matchService.save(match);
        }
        MoveResponse simpTempResponse = MoveResponse.builder().move(moveRequest.getMove()).matchResult(matchScore).build();
        messagingTemplate.convertAndSend("/topic/move/" + matchId, simpTempResponse);
        MoveResponse moveResponse = MoveResponse.builder().validMove(true).matchResult(matchScore).build();
        return ResponseEntity.ok(moveResponse);
    }


//    @PostMapping("/processBot")
//    public ResponseEntity<MoveBotResponse> moveBotProcessing(@RequestBody MoveRequest moveRequest)
//    {
//        Moves.moveOnBoard(moveRequest.getMove(), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Searching.Negamax2(SearchingDepth, -99999999, 99999999);
//        String moveBot = Searching.bestMove;
//        Moves.moveOnBoard(moveBot, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        MoveBotResponse moveBotResponse = MoveBotResponse.builder().moveBot(moveBot).isWhite(!WhiteToMove).build();
//        return ResponseEntity.ok(moveBotResponse);
//    }

    @PostMapping("/idMatchType")
    public ResponseEntity<IdMatchTypeResponse> onlinePage(@RequestBody IdMatchTypeRequest idMatchTypeFromClient) {
        idMatchType = idMatchTypeFromClient.getIdMatchType();
        System.out.println("idMatchType: " + idMatchType);
        List<Matches> matchesList = matchService.getAllMatchesByStatus(MatchStatus.PENDING);
        if (matchesList.isEmpty()) {
            String securedId = RNG.generateSecureId();
            idMatch = securedId;
            System.out.println(securedId);
            matchService.save(Matches.builder().matchId(idMatch).status(MatchStatus.PENDING).build());
            ChessGame chessGame = ChessGame.builder().id(idMatch).build();
            games.put(idMatch, chessGame);
        } else {
            idMatch = matchesList.get(0).getMatchId();
            Matches match = matchService.getMatchByIdMatch(idMatch);
            match.setStatus(MatchStatus.PLAYING);
            matchService.save(match);
            System.out.println("idMatchhhhhhhh: " + idMatch);
        }
        IdMatchTypeResponse idMatchTypeResponse = IdMatchTypeResponse.builder().idMatch(idMatch).build();
        return ResponseEntity.ok(idMatchTypeResponse);
    }

    @PostMapping("/endgame/{matchId}")
    public ResponseEntity<Void> endgameHandling(@PathVariable String matchId, @RequestBody MoveRequest moveRequest) {
        matchService.updateMatchMoves(idMatch, moveRequest.getAllMoves());
        games.remove(matchId);
        return ResponseEntity.ok(null);
    }
}
