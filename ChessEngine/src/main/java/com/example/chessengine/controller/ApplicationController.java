package com.example.chessengine.controller;

import com.example.chessengine.authentication.AuthenticationRequest;
import com.example.chessengine.authentication.AuthenticationResponse;
import com.example.chessengine.authentication.AuthenticationService;
import com.example.chessengine.authentication.RegisterRequest;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.security.BcryptEncoder;
import com.example.chessengine.security.JwtService;
import com.example.chessengine.service.AccountService;
import com.example.chessengine.service.emailSending.MailService;
import gov.nih.nlm.nls.lvg.Util.Str;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping("/public")
public class ApplicationController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
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
    public String handleLogin(@ModelAttribute("account") Accounts account, Model model, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(
                AuthenticationRequest.builder()
                        .gmail(account.getUsername())
                        .password(account.getPassword()).build());

        String token = authenticationResponse.getToken();
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        System.out.println("token::::::::::::::" + token);

        Cookie cookie1 = new Cookie("gmail", account.getUsername());
        cookie1.setPath("/");
        response.addCookie(cookie1);

        // Retrieve the authenticated user
        Accounts authenticatedUser = accountService.getAccountByGmail(account.getUsername());

        // Add the username12 to the model
        model.addAttribute("username", authenticatedUser.getUsername12());
        System.out.println("username" + authenticatedUser.getUsername12());

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

    @GetMapping("changepassword")
    public  String ShowPassword(){ return "/ChangePassword";}
    @GetMapping("/inforuser")
    public String showInformation(Model model, Principal principal) {

        Accounts authenticatedUser = accountService.getAccountByGmail(principal.getName());

        model.addAttribute("email", authenticatedUser.getGmail());
        model.addAttribute("name", authenticatedUser.getUsername12());
        model.addAttribute("birth", authenticatedUser.getDateOfBirth());
        model.addAttribute("gender", authenticatedUser.getGender());
        model.addAttribute("elo", authenticatedUser.getElo());
        model.addAttribute("role", authenticatedUser.getRole());
        model.addAttribute("image", authenticatedUser.getImage());


        return "InformationUser";
    }
    @PostMapping("/update-user")
    public String updateUser(@RequestParam("email") String email,
                             @RequestParam("name") String name,
                             @RequestParam("dateBirth") String dateBirth,
                             @RequestParam("gender") String gender,
                             Principal principal, Model model) throws ParseException {

            Accounts authenticatedUser = accountService.getAccountByGmail(principal.getName());
            authenticatedUser.setGmail(email);
            authenticatedUser.setUsername12(name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateBirth);
            authenticatedUser.setDateOfBirth(parsedDate);
            authenticatedUser.setGender(Boolean.valueOf(gender));

        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dateBirth);
        System.out.println("Gender: " + gender);

            accountService.save(authenticatedUser);
            return "redirect:/inforuser";
    }
    @PostMapping("/save-image")
    @ResponseBody
    public String saveImagePathToDatabase(@RequestBody Map<String, String> imageData, Principal principal) {
        try {
            String imagePath = imageData.get("imagePath");
            accountService.saveImage(principal.getName(), imagePath);
            return "{\"message\": \"Image path saved successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to save image path\"}";
        }
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, Model model) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error_message", "Passwords do not match");
                return "ChangePassword";
            }

            Accounts authenticatedUser = accountService.getAccountByGmail(principal.getName());
            authenticatedUser.setPassword(bcryptEncoder.encode(newPassword));
            accountService.save(authenticatedUser);

            return "redirect:/inforuser";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ChangePassword";
    }

}
