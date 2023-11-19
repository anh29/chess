package com.example.chessengine.dao;

import com.example.chessengine.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Roles, Integer> {
    Optional<Roles> findRolesByRoleName(String roleName);
}
