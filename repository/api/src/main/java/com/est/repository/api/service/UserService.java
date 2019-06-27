package com.est.repository.api.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.est.repository.api.model.User;

public interface UserService extends UserDetailsService {
    Optional<User> getUserByUsername(String username);
    
    User create(User user);
}
