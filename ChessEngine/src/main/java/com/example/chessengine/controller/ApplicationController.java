package com.example.chessengine.controller;

import com.example.chessengine.authentication.AuthenticationRequest;
import com.example.chessengine.authentication.AuthenticationResponse;
import com.example.chessengine.authentication.AuthenticationService;
import com.example.chessengine.authentication.RegisterRequest;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.security.BcryptEncoder;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.emailSending.MailService;
import gov.nih.nlm.nls.lvg.Util.Str;
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
    @Autowired
    private MailService mailService;
    private Accounts currentAccount;
    private final BcryptEncoder bcryptEncoder = new BcryptEncoder();

    @GetMapping()
    public String showDefault() {
//        return "index";
        return "redirect:/public/login";
    }

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("account", new Accounts());
        return "Signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@Valid @ModelAttribute("account") Accounts account) {
//        System.out.println(bcryptEncoder.matches("12345", "$2a$10$HCZFusNAainHMLatJnrSUuHGUowuYA19ViCba9bQFFBUNUiF7VH12"));
        authenticationService.register(RegisterRequest.builder()
                .gmail(account.getUsername()).password(account.getPassword()).build());
//        System.out.println();
        return "redirect:/public/login";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPassword(Model model) {
        model.addAttribute("account", new Accounts());
        return "ForgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String handleForgotPassword(@ModelAttribute("account") Accounts account, Model model, HttpServletResponse response) {
//        Accounts accountGet = accountService.getAccountByGmail(account.getGmail());
//        boolean res = mailService.sendOTP(account.getGmail(), response);
//        if (res) {
//            currentAccount = accountGet;
//            return "redirect:/public/confirmForgotPassword";
//        } else {
//            model.addAttribute("msg", "Account not found!");
//            return "ForgotPassword";
//        }
        return "redirect:/public/confirmForgotPassword";
    }

    @GetMapping("/confirmForgotPassword")
    public String showConfirm() {
        return "ConfirmForgotPassword";
    }

    @PostMapping("/confirmForgotPassword")
    public String handleOTP(@RequestParam("txt_otp") String otp, Model model, HttpServletRequest request) {
//        boolean check = mailService.verifyOtp(currentAccount.getGmail(), otp, request);
//        if (!check) {
//            model.addAttribute("msg", "Wrong otp");
//            return "ConfirmForgotPassword";
//        }
        System.out.println("ajweiojf;oj;ilsj;zncnzsld;vn");
        return "redirect:/public/signup";
    }

    @GetMapping("/resetPassword")
    public String showReset(Model model) {
        return "ResetPassword";
    }

    @PostMapping("/resetPassword")
    public String handleReset(@RequestParam("txt_newPassword") String newPassword) {
        accountService.changePassword(currentAccount, newPassword);
        return "redirect:/public/login";
    }

    @GetMapping("/successMessageReset")
    public String showManageReset(Model model) {
        return "SuccessMessageReset";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("account", new Accounts());
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("account") Accounts account, HttpServletResponse response) {
//        System.out.println("00000000000000000000000000000000000000000000000000000000");
        AuthenticationResponse authenticationResponse = authenticationService.
                authenticate(AuthenticationRequest.builder()
                        .gmail(account.getUsername())
                        .password(account.getPassword()).build());
//        System.out.println("ajwoeifjoajweiof");
        String token = authenticationResponse.getToken();
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setPath("/");
        response.addCookie(cookie);
//        System.out.println("Token from controller: "+ token);
        return "redirect:/play";
    }

    @GetMapping("/learn")
    public String showLearn(Model model, Principal principal, @RequestHeader(value = "request-source", required = false) String requestSource) {
        return "fragments/learn";
    }

    @GetMapping("/fragments/a")
    public String showFrag1() {
        return "fragments/sidebar";
    }

    @GetMapping("/fragments/b")
    public String showFrag2() {
        return "fragments/navigate";
    }

    @GetMapping("/fragments/c")
    public String showFrag3() {
        return "fragments/chessboard";
    }
}
