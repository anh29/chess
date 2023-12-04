package com.example.chessengine.service;

import com.example.chessengine.dao.AccountMatchRepo;
import com.example.chessengine.entity.AccountsMatches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountMatchServiceImpl implements AccountMatchService{
    @Autowired
    private AccountMatchRepo accountMatchRepo;

    @Override
    public void save(AccountsMatches accountsMatches) {
        accountMatchRepo.save(accountsMatches);
    }
}
