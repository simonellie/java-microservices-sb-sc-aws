package com.xantrix.webapp.authserver.security;

import java.util.List;

import lombok.Data;

@Data
public class User {
	private String id;
	private String userId;
	private String password;
	private String active;
	
	private List<String> roles;
}
