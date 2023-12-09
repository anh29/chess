package com.example.chessengine.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleResponse {
    private String id;
    private String fen;
    private String flag;
    private String firstMove;
    private boolean right;
    private boolean done;
}
