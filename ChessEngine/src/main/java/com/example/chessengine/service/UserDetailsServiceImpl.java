package com.example.chessengine.service;

import com.example.chessengine.dao.AccountRepo;
import com.example.chessengine.entity.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String gmail) throws UsernameNotFoundException {
//        System.out.println("ăoiefjolszvnknjselwhawlifc;z");
        Accounts account = accountRepo.findAccountsByGmail(gmail).get();
        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), new ArrayList<>());
    }
}
