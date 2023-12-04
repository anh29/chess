package com.example.chessengine.dao;

import com.example.chessengine.entity.AccountsMatches;
import com.example.chessengine.entity.embeddable.AccountsMatchesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMatchRepo extends JpaRepository<AccountsMatches, AccountsMatchesId> {
}
