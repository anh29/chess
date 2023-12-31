package com.example.chessengine.service;

import com.example.chessengine.entity.Accounts;

import java.util.List;

public interface AccountService {
    Accounts getAccountByGmail(String gmail);
    void save(Accounts accounts);
    void changePassword(Accounts account, String newPassword);
    int getAccountIdByGmail(String gmail);
     void saveImage(String gmail, String imageUrl);
    List<Accounts> findByRole_Id(Integer role_id, Integer status);
}
