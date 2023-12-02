package com.example.chessengine.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class MainController {
    @GetMapping()
    public String showIndex()
    {
        return "redirect:/play";
    }

    @GetMapping("/play")
    public String showLearn(Model model, Principal principal,
                            @RequestHeader(value = "request-source", required = false) String requestSource) {
        if (requestSource == null && principal != null) {
            return "index";
        }
        else
            return "fragments/containHome";
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
            // Use the gameId parameter as needed
            model.addAttribute("idTime", idTime);
            return "PlayOnline";
        } else if (requestSource == null && principal != null) {
            return "PlayOnline";
        } else {
            return "index";
        }
    }

    @GetMapping("/computer")
    public String playComputer(
            Model model,
            Principal principal,
            @RequestParam(name = "id", required = false) String idTime,
            @RequestHeader(value = "request-source", required = false) String requestSource
    ) {
        if (idTime != null) {
            // Use the gameId parameter as needed
            model.addAttribute("idTime", idTime);
            return "PlayComputer";
        } else if (requestSource == null && principal != null) {
            return "PlayComputer";
        } else {
            return "index";
        }
    }
}
