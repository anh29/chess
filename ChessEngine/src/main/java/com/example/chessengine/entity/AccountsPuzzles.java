package com.example.chessengine.entity;

import com.example.chessengine.entity.embeddable.AccountsPuzzlesId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts_puzzles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsPuzzles {
    @EmbeddedId
    private AccountsPuzzlesId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Accounts account;

    @ManyToOne
    @MapsId("puzzleId")
    @JoinColumn(name = "puzzle_id")
    private Puzzles puzzle;

    @Column(name = "status")
    private Integer status;
}
