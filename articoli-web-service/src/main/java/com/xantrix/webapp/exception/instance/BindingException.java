package com.xantrix.webapp.exception.instance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindingException extends Exception {
	private String message;
	
	public BindingException() {
		super();
	}
	
	public BindingException(String message) {
		super(message);
		this.message = message;
	}
}
