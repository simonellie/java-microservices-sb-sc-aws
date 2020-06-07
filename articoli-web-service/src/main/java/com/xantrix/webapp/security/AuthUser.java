package com.xantrix.webapp.security;

import lombok.Data;

import java.util.List;

@Data
public class AuthUser {
    private String id;
    private String userId;
    private String password;
    private String active;
    private List<String> roles;
}
