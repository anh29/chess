package com.example.chessengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/play")
public class MainController {
    @GetMapping()
    public String showIndex()
    {
        return "index";
    }
}
