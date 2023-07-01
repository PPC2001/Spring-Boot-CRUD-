package com.example.demo.exception;

/* 
 It is an exception which is used to indicate a customer with 
 username specified already exists in the database

 */

public class ResourceAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
