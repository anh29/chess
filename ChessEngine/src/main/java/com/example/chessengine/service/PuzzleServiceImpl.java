package com.example.chessengine.service;

import com.example.chessengine.dao.PuzzleRepo;
import com.example.chessengine.entity.Puzzles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PuzzleServiceImpl implements PuzzleService{
    @Autowired
    private final PuzzleRepo puzzleRepo;

    public PuzzleServiceImpl(PuzzleRepo puzzleRepo) {
        this.puzzleRepo = puzzleRepo;
    }

    @Override
    public Puzzles getRandomPuzzle() {
        return puzzleRepo.findRandomPuzzle();
    }
}
