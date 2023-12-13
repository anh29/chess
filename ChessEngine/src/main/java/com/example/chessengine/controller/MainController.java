package com.example.chessengine.controller;

import com.example.chessengine.entity.Accounts;
import com.example.chessengine.entity.Matches;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.MatchService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountService accountService;

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
            String username = principal.getName();
            model.addAttribute("username", extractNameFromEmail(username));

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

            // Retrieve username from Principal
            String username = extractNameFromEmail(principal.getName());
            model.addAttribute("username", username);

            playersInMatches.put(username, idMatch);

//            System.out.println("App: " + ApplicationController.gmail);
//            Accounts account = accountService.getAccountByGmail(ApplicationController.gmail);
//            Matches match = matchService.getMatchByIdMatch(idMatch);
//            if (match == null) {
//                System.out.println("not info please: " + idMatch);
//                match = Matches.builder().matchId(idMatch).status(0).build();
//            } else {
//                match.setStatus(1);
//            }
//            matchService.save(match);
            return "PlayOnline";
        } else if (requestSource == null && principal != null) {
            // Retrieve the match ID for the current player
            String username = extractNameFromEmail(principal.getName());
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
            model.addAttribute("username", extractNameFromEmail(username));

            return "PlayComputer";
        } else if (requestSource == null && principal != null) {
            // Retrieve username from Principal
            String username = principal.getName();
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
            String username = principal.getName();
            model.addAttribute("username", extractNameFromEmail(username));

            return "Tool";
        } else {
            return "fragments/chessboard";
        }
    }



    public static String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        // Tách phần tên từ địa chỉ email
        String[] parts = email.split("@");

        // Kiểm tra xem có ít nhất một phần tử sau khi tách không
        if (parts.length >= 2) {
            return parts[0]; // Trả về phần tử đầu tiên sau khi tách
        } else {
            return null; // Trả về null nếu không thể tách được địa chỉ email
        }
    }


}
