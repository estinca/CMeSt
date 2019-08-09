package com.est.repository.dbm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.est.repository.api.exception.RepositoryException;
import com.est.repository.api.model.Site;
import com.est.repository.api.model.User;
import com.est.repository.api.service.SiteService;
import com.est.repository.api.service.StubService;
import com.est.repository.api.service.UserService;
import com.est.repository.dbm.service.stubs.SiteStubLoader;
import com.est.repository.dbm.service.stubs.UserStubLoader;


@Profile("stubs")
@Service
public class StubServiceDBM implements StubService {

	
	private PasswordEncoder passwordEncoder;
	private UserService userService;
	private SiteService siteService;

	private List<User> users;
	private List<Site> sites;
	
	@Autowired
	public StubServiceDBM(PasswordEncoder passwordEncoder, UserService userService, SiteService siteService) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.siteService = siteService;
	}
	
	@Override
	public void loadStubs() throws RepositoryException {
		
		users = UserStubLoader.loadUsers(passwordEncoder);
		for(User user: users) {
			userService.create(user);
		}
		
		sites = SiteStubLoader.loadSites();
		for(Site site: sites) {
			siteService.create(site);
		}
		
	}

}
