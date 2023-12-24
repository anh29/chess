package com.example.chessengine.service;

import com.example.chessengine.dao.AccountRepo;
import com.example.chessengine.entity.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    @Override
    public void changePassword(Accounts account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(account);
    }

    @Override
    public int getAccountIdByGmail(String gmail) {
        return accountRepo.findAccountsByGmail(gmail).get().getAccountId();
    }

    @Override
    public void saveImage(String gmail, String imageUrl) {
        Accounts account = accountRepo.findByGmail(gmail);
        account.setImage(imageUrl);
        accountRepo.save(account);
    }

}
