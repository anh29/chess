package com.example.chessengine.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleProcessing {
    private LinkedList<String> moves;
    private String flag;
}
