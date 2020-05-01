package com.xantrix.webapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BindingException.class)
	public ResponseEntity<ErrorResponse> exceptionBindingHandler(Exception ex) {
		ErrorResponse errore = new ErrorResponse();
		errore.setCode(HttpStatus.BAD_REQUEST.value());
		errore.setMessage(ex.getMessage());
		return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ErrorResponse> exceptionDeplicateRecordHandler(Exception ex) {
		ErrorResponse errore = new ErrorResponse();
		errore.setCode(HttpStatus.NOT_ACCEPTABLE.value());
		errore.setMessage(ex.getMessage());
		return new ResponseEntity<>(errore, HttpStatus.NOT_ACCEPTABLE);
    }
    
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex)	{
		ErrorResponse errore = new ErrorResponse();
		errore.setMessage("Generic error during request execution.");
		errore.setCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errore, HttpStatus.BAD_REQUEST);
	}
}
