package com.example.chessengine.service;

import com.example.chessengine.entity.Matches;

import java.util.List;

public interface MatchService {
    Matches getMatchByIdMatch(String id);
    List<Matches> getAllMatchesByStatus(int status);
    void updateMatchMoves(String matchId, String newMoves);
    void save (Matches match);
}
