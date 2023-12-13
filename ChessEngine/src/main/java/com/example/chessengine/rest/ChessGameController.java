package com.example.chessengine.rest;

import com.example.chessengine.chessProcessing.BoardGeneration;
import com.example.chessengine.chessProcessing.ChessMoveValidator;
import com.example.chessengine.chessProcessing.Moves;
import com.example.chessengine.chessProcessing.Searching;
import com.example.chessengine.constant.MatchStatus;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.MatchService;
import com.example.chessengine.utility.BackUpChessGame;
import com.example.chessengine.utility.ChessGame;
import com.example.chessengine.utility.MatchCombinedId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/chess")
public class ChessGameController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountMatchService accountMatchService;

    public static ConcurrentHashMap<MatchCombinedId, ChessGame> games = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, BackUpChessGame> backupGames = new ConcurrentHashMap<>();

    private final ChessMoveValidator chessMoveValidator;
    private final SimpMessagingTemplate messagingTemplate;
//    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
//    public static ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<HistoricInfo>();
//    public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
//    public static int SearchingDepth = 4;
    public static String idMatchType;
    public static String idMatch;

    public ChessGameController(ChessMoveValidator chessMoveValidator, SimpMessagingTemplate messagingTemplate) {
        this.chessMoveValidator = chessMoveValidator;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/move/{matchTypeId}/{matchId}")
    public ResponseEntity<MoveResponse> makeMove(@PathVariable String matchTypeId, @PathVariable String matchId, @RequestBody MoveRequest moveRequest)
    {
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(matchTypeId).matchId(matchId).build();
        boolean isValid = chessMoveValidator.isValidMove(moveRequest, games.get(combinedId).WhiteToMove, matchTypeId, matchId);
        MoveResponse moveResponse = MoveResponse.builder().validMove(isValid).matchResult("").build();
        return ResponseEntity.ok(moveResponse);
    }

    @PostMapping("/fen/{matchTypeId}/{matchId}")
    public ResponseEntity<FENResponse> parseFEN(@PathVariable String matchTypeId, @PathVariable String matchId, @RequestBody FENRequest fenRequest) {
        BoardGeneration.initFromFEN(fenRequest.getFenRequest(), matchTypeId, matchId);
        FENResponse fenResponse = FENResponse.builder().build();
        return ResponseEntity.ok(fenResponse);
    }

    @PostMapping("/process/{matchTypeId}/{matchId}")
    public ResponseEntity<MoveResponse> moveProcessing(@PathVariable String matchTypeId, @PathVariable String matchId, @RequestBody MoveRequest moveRequest)
    {
//        System.out.println(moveRequest.getMove());
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(matchTypeId).matchId(matchId).build();
        Moves.moveOnBoard(moveRequest.getMove(), games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK, games.get(combinedId).EP, games.get(combinedId).CWK, games.get(combinedId).CWQ, games.get(combinedId).CBK, games.get(combinedId).CBQ, games.get(combinedId).WhiteToMove, matchTypeId, matchId);
//        System.out.println("WP in process: " + WP);
//        boolean isValid = chessMoveValidator.isValidMove(moveRequest, WhiteToMove);
//        WhiteToMove = !WhiteToMove;
        String matchScore = "";
        if (!games.get(combinedId).WhiteToMove) {
            if (Moves.BlackPossibleMoves(games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK, games.get(combinedId).EP, games.get(combinedId).CWK, games.get(combinedId).CWQ, games.get(combinedId).CBK, games.get(combinedId).CBQ).isEmpty()) {
                if ((Moves.unsafeForBlack(games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK) & games.get(combinedId).BK) != 0) {
                    System.out.println("Black lose");
                    matchScore = "1-0";
                } else {
                    System.out.println("0.5-0.5");
                    matchScore = "0.5-0.5";
                }
            }
        } else {
            if (Moves.WhitePossibleMoves(games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK, games.get(combinedId).EP, games.get(combinedId).CWK, games.get(combinedId).CWQ, games.get(combinedId).CBK, games.get(combinedId).CBQ).isEmpty()) {
                if ((Moves.unsafeForWhite(games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK) & games.get(combinedId).WK) != 0) {
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


    @PostMapping("/processBot/{matchTypeId}/{matchId}")
    public ResponseEntity<MoveBotResponse> moveBotProcessing(@PathVariable String matchTypeId, @PathVariable String matchId, @RequestBody MoveRequest moveRequest)
    {
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(matchTypeId).matchId(matchId).build();
        games.get(combinedId).setSuccess(false);
        Moves.moveOnBoard(moveRequest.getMove(), games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK, games.get(combinedId).EP, games.get(combinedId).CWK, games.get(combinedId).CWQ, games.get(combinedId).CBK, games.get(combinedId).CBQ, games.get(combinedId).WhiteToMove, matchTypeId, matchId);
        backup(matchTypeId, matchId);
        try
        {
            if (games.get(combinedId).SEARCHING_DEPTH != 4) {
                games.get(combinedId).SEARCHING_DEPTH = 4;
            }
            while (Searching.Negamax2(games.get(combinedId).SEARCHING_DEPTH, -99999999, 99999999, matchTypeId, matchId) == 99999999)
            {
                games.get(combinedId).SEARCHING_DEPTH = games.get(combinedId).SEARCHING_DEPTH - 1;
            }
            Searching.Negamax2(games.get(combinedId).SEARCHING_DEPTH, -99999999, 99999999, matchTypeId, matchId);
            games.get(combinedId).setSuccess(true);
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
        } finally {
            if (!games.get(combinedId).isSuccess()) {
                games.get(combinedId).copyFromBackupChessGame(backupGames.get(matchId));
                if (games.get(combinedId).SEARCHING_DEPTH != 1) {
                    games.get(combinedId).SEARCHING_DEPTH = 1;
                }
                Searching.ply = 0;
                Searching.Negamax2(games.get(combinedId).SEARCHING_DEPTH, -99999999, 99999999, matchTypeId, matchId);
            }
        }
        String moveBot = Searching.bestMove;
        Moves.moveOnBoard(moveBot, games.get(combinedId).WP, games.get(combinedId).WN, games.get(combinedId).WB, games.get(combinedId).WR, games.get(combinedId).WQ, games.get(combinedId).WK, games.get(combinedId).BP, games.get(combinedId).BN, games.get(combinedId).BB, games.get(combinedId).BR, games.get(combinedId).BQ, games.get(combinedId).BK, games.get(combinedId).EP, games.get(combinedId).CWK, games.get(combinedId).CWQ, games.get(combinedId).CBK, games.get(combinedId).CBQ, games.get(combinedId).WhiteToMove, matchTypeId, matchId);
        MoveBotResponse moveBotResponse = MoveBotResponse.builder().moveBot(moveBot).isWhite(!games.get(combinedId).WhiteToMove).build();
        return ResponseEntity.ok(moveBotResponse);
    }

    @PostMapping("/idMatchType")
    public ResponseEntity<IdMatchTypeResponse> onlinePage(@RequestBody IdMatchTypeRequest idMatchTypeFromClient) {
        idMatchType = idMatchTypeFromClient.getIdMatchType();
        System.out.println("idMatchType: " + idMatchType);
        if (!Objects.equals(idMatchType, "bot")) {
            List<Matches> matchesList = matchService.getAllMatchesByStatusAndIdMatchType(MatchStatus.PENDING, idMatchType);
            if (matchesList.isEmpty()) {
                String securedId = RNG.generateSecureId();
                idMatch = securedId;
                System.out.println(securedId);
                matchService.save(Matches.builder().matchId(idMatch).idMatchType(idMatchType).status(MatchStatus.PENDING).build());
                MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idMatchType).matchId(idMatch).build();
                ChessGame chessGame = ChessGame.builder().id(idMatch).build();
                games.put(combinedId, chessGame);
            } else {
                idMatch = matchesList.get(0).getMatchId();
                Matches match = matchService.getMatchByIdMatch(idMatch);
                match.setStatus(MatchStatus.PLAYING);
                matchService.save(match);
                System.out.println("idMatchhhhhhhh: " + idMatch);
            }
        } else {
            String securedId = RNG.generateSecureId();
            idMatch = securedId;
            System.out.println(securedId);
            matchService.save(Matches.builder().matchId(idMatch).idMatchType(idMatchType).status(MatchStatus.PLAY_WITH_BOT).build());
            MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idMatchType).matchId(idMatch).build();
            ChessGame chessGame = ChessGame.builder().id(idMatch).build();
            games.put(combinedId, chessGame);
            backupGames.put(idMatch, BackUpChessGame.builder().build());
        }
        IdMatchTypeResponse idMatchTypeResponse = IdMatchTypeResponse.builder().idMatch(idMatch).build();
        return ResponseEntity.ok(idMatchTypeResponse);
    }

    @PostMapping("/endgame/{matchTypeId}/{matchId}")
    public ResponseEntity<Void> endgameHandling(@PathVariable String matchTypeId, @PathVariable String matchId, @RequestBody MoveRequest moveRequest) {
        matchService.updateMatchMovesFirst(matchId, moveRequest.getAllMoves(), moveRequest.getFlag());
        matchService.updateMatchMovesSecond(matchId, moveRequest.getAllMoves(), moveRequest.getFlag());
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(matchTypeId).matchId(matchId).build();
        games.remove(combinedId);
        return ResponseEntity.ok(null);
    }

    private void backup(String idMatchType, String id) {
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idMatchType).matchId(id).build();
        backupGames.get(id).copyFromChessGame(games.get(combinedId));
    }
}
