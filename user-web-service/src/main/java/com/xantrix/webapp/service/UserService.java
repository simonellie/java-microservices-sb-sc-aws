package com.xantrix.webapp.service;

import com.xantrix.webapp.model.User;

import java.util.List;

public interface UserService {
	List<User> getUsers();
	User getUserById(String UserId);
	void save(User utente);
	void delete(User utente);
}
