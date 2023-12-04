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
        if (score.equals("1-0")) {
            for (int i = 0; i < totalMoves; i++) {
                if (i < oldMovesArray.length) {
                    combinedMovesArray[i] = oldMovesArray[i];
                } else {
                    combinedMovesArray[i] = newMovesArray[i - oldMovesArray.length];
                }
            }
        } else if (score.equals("0-1")) {
            for (int i = 0; i < totalMoves; i++) {
                if (i < newMovesArray.length) {
                    combinedMovesArray[i] = newMovesArray[i];
                } else {
                    combinedMovesArray[i] = oldMovesArray[i - newMovesArray.length];
                }
            }
        }
        String allMoves = String.join(", ", combinedMovesArray);
        match.setMoves(allMoves);
        matchRepo.save(match);
    }


}
