package com.example.chessengine.service;

import com.example.chessengine.entity.Accounts;

public interface AccountService {
    Accounts getAccountByGmail(String gmail);
    void save(Accounts accounts);
}
