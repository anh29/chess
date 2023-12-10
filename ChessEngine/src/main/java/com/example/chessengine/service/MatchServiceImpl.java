package com.example.chessengine.service;

import com.example.chessengine.constant.Side;
import com.example.chessengine.dao.MatchRepo;
import com.example.chessengine.entity.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    public List<Matches> getAllMatchesByStatusAndIdMatchType(int status, String idMatchType) {
        return matchRepo.findAllByStatusAndIdMatchType(status, idMatchType);
    }

    @Override
    public void updateMatchMovesFirst(String matchId, String moves, String flag) {
        Matches match = matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        if (match.getMoves().isEmpty()) {
            match.setMoves(moves);
            matchRepo.save(match);
        }
    }

    @Override
    public void updateMatchMovesSecond(String matchId, String moves, String flag) {
        Matches match = matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        if (!match.getMoves().isEmpty()) {
            String oldMoves = match.getMoves();
            LinkedList<String> oldMovesArray = new LinkedList<>(Arrays.asList(oldMoves.split(", ")));
            LinkedList<String> newMovesArray = new LinkedList<>(Arrays.asList(moves.split(", ")));
            int totalMoves = oldMovesArray.size() + newMovesArray.size();
            LinkedList<String> combinedMovesArray = new LinkedList<>();
            while (combinedMovesArray.size() < totalMoves) {
                if (Objects.equals(flag, Side.WHITE)) {
                    if (!newMovesArray.isEmpty())
                    {
                        combinedMovesArray.addLast(newMovesArray.getFirst());
                        newMovesArray.removeFirst();
                    }
                    if (!oldMovesArray.isEmpty())
                    {
                        combinedMovesArray.addLast(oldMovesArray.getFirst());
                        oldMovesArray.removeFirst();
                    }
                } else {
                    if (!oldMovesArray.isEmpty())
                    {
                        combinedMovesArray.addLast(oldMovesArray.getFirst());
                        oldMovesArray.removeFirst();
                    }
                    if (!newMovesArray.isEmpty())
                    {
                        combinedMovesArray.addLast(newMovesArray.getFirst());
                        newMovesArray.removeFirst();
                    }
                }
            }

            String allMoves = String.join(", ", combinedMovesArray);
            match.setMoves(allMoves);

            matchRepo.save(match);
        }
    }
}
