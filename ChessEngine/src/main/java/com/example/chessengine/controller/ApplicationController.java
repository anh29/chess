package com.example.chessengine.controller;

import com.example.chessengine.authentication.AuthenticationRequest;
import com.example.chessengine.authentication.AuthenticationResponse;
import com.example.chessengine.authentication.AuthenticationService;
import com.example.chessengine.authentication.RegisterRequest;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.security.BcryptEncoder;
import com.example.chessengine.service.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/public")
public class ApplicationController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationService authenticationService;
    private BcryptEncoder bcryptEncoder = new BcryptEncoder();

    @GetMapping("/")
    public String showDefault()
    {
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignup(Model model)
    {
        model.addAttribute("account", new Accounts());
        return "Signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@Valid @ModelAttribute("account") Accounts account)
    {
//        System.out.println(bcryptEncoder.matches("12345", "$2a$10$HCZFusNAainHMLatJnrSUuHGUowuYA19ViCba9bQFFBUNUiF7VH12"));
        authenticationService.register(RegisterRequest.builder()
                .gmail(account.getUsername()).password(account.getPassword()).build());
//        System.out.println();
        return "redirect:/public/login";
    }

    @GetMapping("/login")
    public String showLogin(Model model)
    {
        model.addAttribute("account", new Accounts());
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("account") Accounts account, HttpServletResponse response)
    {
//        System.out.println("00000000000000000000000000000000000000000000000000000000");
        AuthenticationResponse authenticationResponse = authenticationService.
                authenticate(AuthenticationRequest.builder()
                        .gmail(account.getUsername())
                        .password(account.getPassword()).build());
//        System.out.println("ajwoeifjoajweiof");
        String token = authenticationResponse.getToken();
        Cookie cookie = new Cookie("jwtToken",token);
        cookie.setPath("/");
        response.addCookie(cookie);
//        System.out.println("Token from controller: "+ token);
        return "redirect:/learn";
    }

    @GetMapping("/learn")
    public String showLearn(Model model, Principal principal, @RequestHeader(value = "request-source", required = false) String requestSource)
    {
        return "fragments/learn";
    }

}