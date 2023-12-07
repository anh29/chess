package com.example.chessengine.service;

import com.example.chessengine.dao.MatchRepo;
import com.example.chessengine.entity.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService{
    @Autowired
    private MatchRepo matchRepo;

    @Override
    public void save(Matches match) {
        matchRepo.save(match);
    }

    @Override
    public Matches getMatchByIdMatch(String id) {
        return matchRepo.findMatchesByMatchId(id);
    }

    @Override
    public List<Matches> getAllMatchesByStatus(int status) {
        return matchRepo.findAllByStatus(status);
    }

    @Override
    public void updateMatchMoves(String matchId, String newMoves) {
        Matches match = matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        String oldMoves = match.getMoves();
        String score = match.getScore();
        String[] oldMovesArray = oldMoves.split(", ");
        String[] newMovesArray = newMoves.split(", ");
        int totalMoves = oldMovesArray.length + newMovesArray.length;
        String[] combinedMovesArray = new String[totalMoves];
        int j = 0;
        for (int i = 0; i < totalMoves / 2; i += 2) {
            if (score.equals("1-0")) {
                combinedMovesArray[i] = oldMovesArray[j];
                combinedMovesArray[i + 1] = newMovesArray[j];
                j++;
            } else if (score.equals("0-1")) {
                combinedMovesArray[i] = newMovesArray[j];
                combinedMovesArray[i + 1] = oldMovesArray[j];
                j++;
            }
            if (totalMoves % 2 != 0) {
                combinedMovesArray[totalMoves - 1] = oldMovesArray[oldMovesArray.length - 1];
            }
        }
//        if (score.equals("1-0")) {
//            for (int i = 0; i < totalMoves / 2; i += 2) {
//                combinedMovesArray[i] = oldMovesArray[j];
//                combinedMovesArray[i + 1] = newMovesArray[j];
//                j++;
//            }
//            if (totalMoves % 2 != 0) {
//                combinedMovesArray[totalMoves - 1] = oldMovesArray[oldMovesArray.length - 1];
//            }
//        } else if (score.equals("0-1")) {
//            for (int i = 0; i < totalMoves / 2; i += 2) {
//                combinedMovesArray[i] = newMovesArray[j];
//                combinedMovesArray[i + 1] = oldMovesArray[j];
//                j++;
//            }
//            if (totalMoves % 2 != 0) {
//                combinedMovesArray[totalMoves - 1] = oldMovesArray[oldMovesArray.length - 1];
//            }
//        }
        String allMoves = String.join(", ", combinedMovesArray);
        match.setMoves(allMoves);
        matchRepo.save(match);
    }


}
