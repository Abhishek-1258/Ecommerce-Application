package com.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistsException(String message) {
		super(message);
	}
	
	

}
