package com.example.chessengine.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptEncoder implements PasswordEncoder {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
//        return rawPassword.toString();
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        System.out.println("----------------------------------------------------------------------------------");
//        return rawPassword.toString().equals(encodedPassword);
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
