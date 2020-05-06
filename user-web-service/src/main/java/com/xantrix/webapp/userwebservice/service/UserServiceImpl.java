package com.xantrix.webapp.userwebservice.service;

import java.util.List;

import com.xantrix.webapp.userwebservice.model.User;
import com.xantrix.webapp.userwebservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(String UserId) {
		return userRepository.findByUserId(UserId);
	}

	@Override
	public void save(User utente) {
		userRepository.save(utente);
	}

	@Override
	public void delete(User utente)	{
		userRepository.delete(utente);
	}
}
