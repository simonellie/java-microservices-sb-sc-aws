package com.xantrix.webapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException  extends Exception {
	private String message;
	
	public DuplicateException()	{
		super();
	}
	
	public DuplicateException(String message) {
		super(message);
		this.message = message;
	}
}
