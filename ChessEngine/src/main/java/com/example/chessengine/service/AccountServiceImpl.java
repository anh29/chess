package com.example.chessengine.service;

import com.example.chessengine.dao.AccountRepo;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.security.BcryptEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepo accountRepo;
    @Override
    public Accounts getAccountByGmail(String gmail) {
//        System.out.println("plsessssssssssssssssssss");
        return accountRepo.findAccountsByGmail(gmail).get();
    }

    @Override
    public void save(Accounts account) {
        Date currentDateAsDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        account.setCreatedDate(currentDateAsDate);
//        account.setPassword(BcryptEncoder.hashPassword(account.getPassword()));
        accountRepo.save(account);
    }


}
