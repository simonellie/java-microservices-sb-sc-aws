package com.xantrix.webapp.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class AuthEntryPoint extends BasicAuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		// Authentication failed, send error response.
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 : " + authException.getMessage());
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("REAME");
		super.afterPropertiesSet();
	}
}
