package com.example.chessengine.authentication;

import com.example.chessengine.dao.AccountRepo;
import com.example.chessengine.dao.RoleRepo;
import com.example.chessengine.entity.Accounts;
import com.example.chessengine.entity.Roles;
import com.example.chessengine.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepo roleRepo;

    public AuthenticationResponse register(RegisterRequest request) {
//        var user = Account.builder().username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
        Roles role = roleRepo.findRolesByRoleName("player").orElseThrow();
//        System.out.println(role + "ăoiejfoiwajefo");
        var account = new Accounts(request.getGmail(), passwordEncoder.encode(request.getPassword()), role, 1500);
        accountRepo.save(account);
//
        var jwtToken = jwtService.generateToken(account);
//        System.out.println("no token " + jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        System.out.println("*************************************");
//        System.out.println(request.getGmail());
//        System.out.println(request.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getGmail(),
                        request.getPassword()
                )
        );
        var account = accountRepo.findAccountsByGmail(request.getGmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
