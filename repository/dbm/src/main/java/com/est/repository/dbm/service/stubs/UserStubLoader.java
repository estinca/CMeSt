package com.est.repository.dbm.service.stubs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.est.repository.api.model.Role;
import com.est.repository.api.model.User;

public final class UserStubLoader {

	public static List<User> loadUsers(PasswordEncoder passwordEncoder) {
		List<User> users = new ArrayList<User>();
		
		User user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("user"));
		user.setRoles(Arrays.asList(Role.USER));
		
		users.add(user);
		
		return users;
	}
}
