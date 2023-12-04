package com.example.chessengine.rest;

import com.example.chessengine.controller.ApplicationController;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.MatchService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ExceptionHandling {
    @Autowired
    private AccountMatchService accountMatchService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MatchService matchService;
    private static final int MAX_PLAYERS = 2;
    private static final AtomicInteger playerCounter = new AtomicInteger(0);

    @PostMapping("online/{idType}/flagProcessing")
    public ResponseEntity<String> flagProcessing(@PathVariable String idType, HttpServletRequest request) {
        int currentPlayer = playerCounter.incrementAndGet();
        if (currentPlayer <= MAX_PLAYERS) {
            String playSide = (currentPlayer == 1) ? "white" : "black";

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                System.out.println("in request cookie 22222222222");
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName());
                    if (cookie.getName().equals("gmail")) {
                        String gmail = cookie.getValue();
                        // Use the token as needed
                        System.out.println("in request cookie");
                        Accounts account = accountService.getAccountByGmail(gmail);
                        Matches match = matchService.getMatchByIdMatch(ChessGameController.idMatch);
                        AccountsMatchesId accountsMatchesId = AccountsMatchesId.builder().accountId(account.getAccountId()).matchId(ChessGameController.idMatch).build();
                        System.out.println(ChessGameController.idMatch);
                        System.out.println(account.getAccountId());
                        AccountsMatches accountMatch = AccountsMatches.builder().id(accountsMatchesId).flag(playSide).account(account).match(match).build();
                        accountMatchService.save(accountMatch);
                        System.out.println(accountMatch.toString());
                    }
                }
            }


            return ResponseEntity.ok(playSide);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
        }
    }
}
