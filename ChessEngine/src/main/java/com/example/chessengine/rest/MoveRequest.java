package com.example.chessengine.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {
    private String move;
    private String allMoves;
    private String flag;

    public MoveRequest(String move) {
        this.move = move;
    }
}
