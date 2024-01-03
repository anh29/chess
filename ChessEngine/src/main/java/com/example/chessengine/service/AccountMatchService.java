package com.example.chessengine.service;

import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;

import java.util.Optional;

public interface AccountMatchService {
    void save(AccountsMatches accountsMatches);

    Optional<AccountsMatches> getById(AccountsMatchesId accountsMatchesId);
}
