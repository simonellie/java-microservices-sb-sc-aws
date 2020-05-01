package com.xantrix.webapp.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private Date date = new Date();
	private int code;
	private String message;
}
