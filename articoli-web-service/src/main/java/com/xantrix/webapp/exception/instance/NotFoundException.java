package com.xantrix.webapp.exception.instance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException  extends Exception {
	private String message = "Elemento Ricercato Non Trovato!";
	
	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
