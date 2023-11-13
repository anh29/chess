package com.example.chessengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    @GetMapping("/")
    public String HelloWorld()
    {
        return "Login.html";
    }
}
