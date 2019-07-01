package com.est.repository.dbm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.est.repository.api.exception.RepositoryException;
import com.est.repository.api.model.User;
import com.est.repository.api.service.SetupService;
import com.est.repository.api.service.StubService;
import com.est.repository.api.service.UserService;
import com.est.repository.dbm.service.stubs.UserStubLoader;


//@Profile("stubs")
//@Service
public class StubServiceDBM implements StubService {

	
	private PasswordEncoder passwordEncoder;
	private UserService userService;
	private SetupService setupService;

	private List<User> users;
	
	//TODO userservice
	@Autowired
	public StubServiceDBM(PasswordEncoder passwordEncoder, SetupService setupService, UserService userService) {
		this.passwordEncoder = passwordEncoder;
		this.setupService = setupService;
		this.userService = userService;
	}
	
	@Override
	public void loadStubs() throws RepositoryException {
		setupService.setupRepository();
		
		users = UserStubLoader.loadUsers(passwordEncoder, userService);
	}

}
