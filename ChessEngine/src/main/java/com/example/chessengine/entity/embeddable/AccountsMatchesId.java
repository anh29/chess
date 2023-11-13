package com.example.chessengine.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AccountsMatchesId implements Serializable {
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "match_id")
    private Integer matchId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsMatchesId that = (AccountsMatchesId) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(matchId, that.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, matchId);
    }
}
