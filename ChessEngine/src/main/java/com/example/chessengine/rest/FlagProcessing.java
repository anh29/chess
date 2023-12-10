package com.example.chessengine.rest;

import com.example.chessengine.entity.Accounts;
import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.MatchService;
import com.example.chessengine.utility.MatchCombinedId;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlagProcessing {
    @Autowired
    private AccountMatchService accountMatchService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MatchService matchService;

    @PostMapping("online/{idType}/flagProcessing/{idMatch}")
    public ResponseEntity<String> flagProcessing(@PathVariable String idType, @PathVariable String idMatch, HttpServletRequest request) {
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idType).matchId(idMatch).build();
        int currentPlayer = ChessGameController.games.get(combinedId).counter;
        if (currentPlayer < ChessGameController.games.get(combinedId).MAX_PLAYERS) {
            String playSide = (currentPlayer == 0) ? "white" : "black";

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
            ChessGameController.games.get(combinedId).counter = currentPlayer + 1;
            System.out.println("counterrrrrrrrrr: " + ChessGameController.games.get(combinedId).counter);

            return ResponseEntity.ok(playSide);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
        }
    }
}
