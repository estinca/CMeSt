package com.est.repository.dbm.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.est.repository.api.model.User;
import com.est.repository.api.service.UserService;
import com.est.repository.dbm.converter.UserConverterRDBMS;
import com.est.repository.dbm.dao.UserDAO;

@Service
@Transactional
public class UserServiceDBM implements UserService {

    private final UserDAO userDAO;
    private final UserConverterRDBMS converter;

    @Autowired
    public UserServiceDBM (UserDAO userDAO, UserConverterRDBMS converter) {
        this.userDAO = userDAO;
        this.converter = converter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " username not found"));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDAO.findByUsername(username).map(converter::fromDB);
    }

    @Override
	public User create(User user) {
		return converter.fromDB(userDAO.saveAndFlush(converter.toDB(user, false)));
		
	}
}
