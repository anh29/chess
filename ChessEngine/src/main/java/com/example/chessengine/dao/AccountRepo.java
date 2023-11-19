package com.example.chessengine.dao;

import com.example.chessengine.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepo extends JpaRepository<Accounts, Integer> {
    Optional<Accounts> findAccountsByGmail(String gmail);

    void deleteAccountsByGmail(String gmail);

    void deleteAccountsByAccountId(Integer id);
}
