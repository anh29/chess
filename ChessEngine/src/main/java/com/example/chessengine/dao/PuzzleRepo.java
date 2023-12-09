package com.example.chessengine.dao;

import com.example.chessengine.entity.Puzzles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PuzzleRepo extends JpaRepository<Puzzles, String> {

    @Query(value = "SELECT * FROM puzzles ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Puzzles findRandomPuzzle();
}
