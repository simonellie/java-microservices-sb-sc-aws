package com.xantrix.webapp.userwebservice.controller;

import java.util.List;

import javax.validation.Valid;

import com.xantrix.webapp.userwebservice.model.User;
import com.xantrix.webapp.userwebservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.userwebservice.exception.BindingException;
import com.xantrix.webapp.userwebservice.exception.NotFoundException;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ResourceBundleMessageSource errMessage;
	
	@GetMapping(value = "/search/all")
	public List<User> getAllUser() {
		return userService.getUsers();
	}
	
	@GetMapping(value = "/search/userid/{userId}")
	public User getUtente(@PathVariable("userId") String UserId) throws NotFoundException {
		User retVal = userService.getUserById(UserId);
		
		if (retVal == null) {
			String ErrMsg = String.format("L'utente %s non è stato trovato!", UserId);
			logger.warn(ErrMsg);
			throw new NotFoundException(ErrMsg);
		}
		
		return retVal;
	}
	
	@PostMapping(value = "/add")
	public ResponseEntity<?> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) throws BindingException {
		if (bindingResult.hasErrors()) {
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			logger.warn(MsgErr);
			throw new BindingException(MsgErr);
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userService.save(user);

		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Inserimento Utente " + user.getUserId() + " Eseguita Con Successo");
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String UserId) throws NotFoundException {
		User utente = userService.getUserById(UserId);

		if (utente == null) {
			String MsgErr = String.format("Utente %s non presente in anagrafica! ",UserId);
			logger.warn(MsgErr);
			throw new NotFoundException(MsgErr);
		}

		userService.delete(utente);
		
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Utente " + UserId + " Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
}
