package com.example.chessengine.dao;

import com.example.chessengine.entity.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<Matches, String> {
    Matches findMatchesByMatchId(String matchId);
    List<Matches> findAllByStatus(int status);

    @Modifying
    @Query("UPDATE Matches m SET m.moves = CONCAT(m.moves, :newMoves) WHERE m.matchId = :matchId")
    void updateMoves(@Param("matchId") Long matchId, @Param("newMoves") String newMoves);
}
