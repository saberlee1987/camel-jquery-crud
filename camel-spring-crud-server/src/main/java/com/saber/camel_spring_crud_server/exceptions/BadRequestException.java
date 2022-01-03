package com.saber.camel_spring_crud_server.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadRequestException extends RuntimeException{
	private String fieldName;
	private String detailMessage;
	
	public BadRequestException(String fieldName, String detailMessage) {
		this.fieldName = fieldName;
		this.detailMessage = detailMessage;
	}
	
	public BadRequestException(String message, String fieldName, String detailMessage) {
		super(message);
		this.fieldName = fieldName;
		this.detailMessage = detailMessage;
	}
	
	public BadRequestException(String message, Throwable cause, String fieldName, String detailMessage) {
		super(message, cause);
		this.fieldName = fieldName;
		this.detailMessage = detailMessage;
	}
	
	public BadRequestException(Throwable cause, String fieldName, String detailMessage) {
		super(cause);
		this.fieldName = fieldName;
		this.detailMessage = detailMessage;
	}
	
	public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String fieldName, String detailMessage) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.fieldName = fieldName;
		this.detailMessage = detailMessage;
	}
	
}
