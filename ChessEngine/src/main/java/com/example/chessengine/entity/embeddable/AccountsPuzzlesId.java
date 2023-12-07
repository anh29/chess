package com.example.chessengine.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsPuzzlesId implements Serializable {
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "puzzle_id")
    private String puzzleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsPuzzlesId that = (AccountsPuzzlesId) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(puzzleId, that.puzzleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, puzzleId);
    }
}
