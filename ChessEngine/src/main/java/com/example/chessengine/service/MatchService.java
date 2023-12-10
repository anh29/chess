package com.example.chessengine.service;

import com.example.chessengine.entity.Matches;

import java.util.List;

public interface MatchService {
    Matches getMatchByIdMatch(String id);
    List<Matches> getAllMatchesByStatusAndIdMatchType(int status, String idMatchType);
    void updateMatchMovesFirst(String matchId, String moves, String flag);
    void updateMatchMovesSecond(String matchId, String moves, String flag);

    void save (Matches match);
}
