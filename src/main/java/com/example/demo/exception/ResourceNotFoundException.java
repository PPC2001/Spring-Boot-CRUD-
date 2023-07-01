package com.example.demo.exception;

/*
 It is an exception which used to indicate when an Customer is looked up but not found. 
 When an ResourceNotFoundException is thrown,
 this extra tidbit of Spring MVC configuration is used to render an HTTP 404:
 
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
