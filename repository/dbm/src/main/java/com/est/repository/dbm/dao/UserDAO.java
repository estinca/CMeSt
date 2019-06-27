package com.est.repository.dbm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.est.repository.dbm.domain.UserDB;

@Repository
public interface UserDAO extends JpaRepository<UserDB, String> {
    Optional<UserDB> findByUsername(String username);
}
