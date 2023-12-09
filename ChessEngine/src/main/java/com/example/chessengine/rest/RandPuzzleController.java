package com.example.chessengine.rest;

import com.example.chessengine.entity.Puzzles;
import com.example.chessengine.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/puzzle")
public class RandPuzzleController {
    @Autowired
    private final PuzzleService puzzleService;

    private final ConcurrentHashMap<String, PuzzleProcessing> puzzlesMap = new ConcurrentHashMap<>();

    public RandPuzzleController(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @GetMapping("/random")
    public ResponseEntity<PuzzleResponse> getRandomPuzzle() {
        Puzzles puzzle = puzzleService.getRandomPuzzle();

        String fenString = puzzle.getFen();
        String[] fenParts = fenString.split(" ");

        String position = fenParts[0];         // The position of the pieces on the board
        String activeColor = fenParts[1];
        String flag = (Objects.equals(activeColor, "w")) ? "black" : "white";

        String allMoves = puzzle.getSolution();
        String[] moveSeq = allMoves.split(" ");
        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(moveSeq));
        var x = convertMoveFormat(moveSeq[0]);

        puzzlesMap.put(puzzle.getPuzzleId(), PuzzleProcessing.builder().moves(linkedList).flag(flag).build());
        PuzzleResponse puzzleResponse = PuzzleResponse.builder().id(puzzle.getPuzzleId()).fen(puzzle.getFen()).flag(flag).firstMove(moveSeq[0]).build();
        puzzlesMap.get(puzzle.getPuzzleId()).getMoves().removeFirst();
        
        return ResponseEntity.ok(puzzleResponse);
    }
    
    @PostMapping("/puzzleProcessing")
    public ResponseEntity<PuzzleResponse> handlePuzzleProcessing(@RequestBody MoveRequest moveRequest)
    {
        PuzzleResponse puzzleResponse = PuzzleResponse.builder().build();
        String checking = convertMoveFormat(puzzlesMap.get(moveRequest.getId()).getMoves().getFirst());
        if (Objects.equals(moveRequest.getMove(), checking)) {
            puzzleResponse.setRight(true);
            puzzlesMap.get(moveRequest.getId()).getMoves().removeFirst();
            if (puzzlesMap.get(moveRequest.getId()).getMoves().isEmpty()) {
                puzzleResponse.setDone(true);
                return ResponseEntity.ok(puzzleResponse);
            }
            puzzleResponse.setFirstMove(puzzlesMap.get(moveRequest.getId()).getMoves().getFirst());
            puzzlesMap.get(moveRequest.getId()).getMoves().removeFirst();
        } else {
            puzzleResponse.setRight(false);
        }
        puzzleResponse.setDone(puzzlesMap.get(moveRequest.getId()).getMoves().isEmpty());
        return ResponseEntity.ok(puzzleResponse);
    }

    private String convertMoveFormat(String move) {
        char sourceFile = move.charAt(0);
        char sourceRank = move.charAt(1);
        char targetFile = move.charAt(2);
        char targetRank = move.charAt(3);

        int sourceFileNumber = sourceFile - 'a';
        int sourceRankNumber = '8' - sourceRank;
        int targetFileNumber = targetFile - 'a';
        int targetRankNumber = '8' - targetRank;

        return "" + sourceRankNumber + sourceFileNumber + targetRankNumber + targetFileNumber;
    }
}
