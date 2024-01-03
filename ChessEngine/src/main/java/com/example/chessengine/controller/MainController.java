package com.example.chessengine.controller;

import com.example.chessengine.entity.Accounts;
import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;
import com.example.chessengine.rest.ChessGameController;
import com.example.chessengine.service.AccountMatchService;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.MatchService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMatchService accountMatchService;

    private Map<String, String> playersInMatches = new HashMap<>();

    @GetMapping()
    public String showIndex()
    {
        return "redirect:/play";
    }

    @GetMapping("/play")
    public String showLearn(Model model, Principal principal,
                            @RequestHeader(value = "request-source", required = false) String requestSource) {
        if (requestSource == null && principal != null) {
            String gmail = principal.getName();
            String username = accountService.getAccountByGmail(gmail).getUsername12();

            model.addAttribute("username", username);
            return "index";
        } else {
            return "fragments/containHome";
        }
    }

    @GetMapping("/puzzles")
    public String showPuzzles(Model model, Principal principal,
                            @RequestHeader(value = "request-source", required = false) String requestSource) {
        if (requestSource == null && principal != null) {
            return "Puzzles";
        }
        else
            return "fragments/containPuzzles";
    }

    @GetMapping("/online/{idTime}/{idMatch}")
    public String play(
            Model model,
            Principal principal,
            @PathVariable String idTime,
            @PathVariable String idMatch,
            @RequestHeader(value = "request-source", required = false) String requestSource
    ) {
        if (idTime != null) {
            model.addAttribute("idTime", idTime);

            String gmail = principal.getName();
            String username = accountService.getAccountByGmail(gmail).getUsername12();

            model.addAttribute("username", username);

            return "PlayOnline";
        } else if (requestSource == null && principal != null) {
            String gmail = principal.getName();
            String username = accountService.getAccountByGmail(gmail).getUsername12();
            String matchId = playersInMatches.get(username);

            // Add the match ID and player's username to the model
            model.addAttribute("matchId", matchId);

            return "PlayOnline";
        } else {
            return "index";
        }
    }

    @GetMapping("/computer/{idMatchType}/{idMatch}")
    public String playComputer(
            Model model,
            Principal principal,
            @PathVariable String idMatchType,
            @PathVariable String idMatch,
            @RequestHeader(value = "request-source", required = false) String requestSource
    ) {
        if (idMatchType != null) {
            model.addAttribute("idTime", idMatchType);

            // Retrieve username from Principal
            String username = principal.getName();
//            model.addAttribute("username", extractNameFromEmail(username));

            return "PlayComputer";
        } else if (requestSource == null && principal != null) {
            // Retrieve username from Principal
            String gmail = principal.getName();
            String username = accountService.getAccountByGmail(gmail).getUsername12();
            model.addAttribute("username", username);

            return "PlayComputer";
        } else {
            return "index";
        }
    }

    @GetMapping("/tool")
    public String showTool(Model model, Principal principal,
                            @RequestHeader(value = "request-source", required = false) String requestSource) {
        if (requestSource == null && principal != null) {
            String gmail = principal.getName();
            String username = accountService.getAccountByGmail(gmail).getUsername12();
            model.addAttribute("username", username);

            return "Tool";
        } else {
            return "fragments/chessboard";
        }
    }
}
