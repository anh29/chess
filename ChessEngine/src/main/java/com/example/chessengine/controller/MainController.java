package com.example.chessengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/online")
    public String play(Model model, Principal principal,
                       @RequestHeader(value = "request-source", required = false) String requestSource) {
        if (requestSource == null && principal != null) {
            return "PlayOnline";
        }
        else
            return "fragments/containPlay";
    }
}
