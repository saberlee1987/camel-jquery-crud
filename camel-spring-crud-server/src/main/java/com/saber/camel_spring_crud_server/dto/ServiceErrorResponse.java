package com.saber.camel_spring_crud_server.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.List;

@Data
public class ServiceErrorResponse {
	private Integer code;
	private String message;
	@JsonRawValue
	private String originalMessage;
	private List<ValidationDto> validationDetails;
}
