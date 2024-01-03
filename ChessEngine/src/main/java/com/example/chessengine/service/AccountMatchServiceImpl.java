package com.example.chessengine.service;

import com.example.chessengine.dao.AccountMatchRepo;
import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountMatchServiceImpl implements AccountMatchService{
    @Autowired
    private AccountMatchRepo accountMatchRepo;

    @Override
    public void save(AccountsMatches accountsMatches) {
        accountMatchRepo.save(accountsMatches);
    }

    @Override
    public Optional<AccountsMatches> getById(AccountsMatchesId accountsMatchesId) {
        return accountMatchRepo.findById(accountsMatchesId);
    }
}
