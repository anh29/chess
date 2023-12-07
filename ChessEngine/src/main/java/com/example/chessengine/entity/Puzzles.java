package com.example.chessengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "puzzles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Puzzles {
    @Id
    @Column(name = "puzzle_id")
    private String puzzleId;

    @Column(name = "fen")
    private String fen;

    @Column(name = "solution")
    private String solution;

    @Column(name = "elo")
    private int elo;

    @OneToMany(mappedBy = "puzzle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountsPuzzles> accountsPuzzlesSet;
}
