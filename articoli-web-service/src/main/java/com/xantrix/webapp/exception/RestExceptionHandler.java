package com.xantrix.webapp.exception;

import com.xantrix.webapp.exception.instance.BindingException;
import com.xantrix.webapp.exception.instance.DuplicateException;
import com.xantrix.webapp.exception.instance.NotFoundException;
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
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ErrorResponse> exceptionDeplicateRecordHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.NOT_ACCEPTABLE.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
    
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex)	{
		ErrorResponse error = new ErrorResponse();
		error.setMessage("Generic error during request execution.");
		error.setCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
