package com.saber.camel_spring_crud_server.exceptions;

public class ResourceDuplicationException extends RuntimeException{
	public ResourceDuplicationException() {
	}
	
	public ResourceDuplicationException(String message) {
		super(message);
	}
	
	public ResourceDuplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResourceDuplicationException(Throwable cause) {
		super(cause);
	}
	
	public ResourceDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
