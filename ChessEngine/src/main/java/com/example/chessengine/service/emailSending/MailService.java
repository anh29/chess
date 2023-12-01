package com.example.chessengine.service.emailSending;

import com.example.chessengine.dao.AccountRepo;
import com.example.chessengine.entity.Accounts;
import jakarta.mail.Message;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AccountRepo accountRepo;

    public boolean sendOTP(String gmail, HttpServletResponse response) {
        Accounts account = accountRepo.findByGmail(gmail);
        if (account != null) {
            String otp = OtpGenerator.generateOtp();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(gmail);
            message.setSubject("Your One-Time Password (OTP) for Password Reset");
            String[] parts = gmail.split("@");
            message.setText("Dear " + parts[0] + ",\n" +
                    "You recently requested a password reset for your account. To complete this process, please use the following one-time password (OTP):\n" +
                    "Your OTP: " + otp + "\n" +
                    "This OTP is valid for a limited time (90 seconds). Please ensure you use it promptly to secure your account. If you did not initiate this password reset, please contact our support team immediately.\n" +
                    "Thank you for choosing our services.\n" +
                    "Best regards,\n" +
                    "One-Man-Army Chess Engine\n" +
                    "Support Team");
            javaMailSender.send(message);

            Cookie cookie = new Cookie("otpCookie", otp);
            cookie.setPath("/");
            cookie.setMaxAge(90);
            response.addCookie(cookie);

            return true;
        } else {
            return false;
        }
    }

    public boolean verifyOtp(String email, String userOtp, HttpServletRequest request) {
        String cachedOtp = getOtpFromCookie(request);
//        System.out.println("Otp from cookie: " + cachedOtp);
        return userOtp.equals(cachedOtp);
    }

    private String getOtpFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("otpCookie")){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
