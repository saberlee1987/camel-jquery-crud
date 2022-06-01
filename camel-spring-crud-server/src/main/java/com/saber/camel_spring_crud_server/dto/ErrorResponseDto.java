package com.saber.camel_spring_crud_server.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

import java.util.List;

@Data
public class ErrorResponseDto {
	private Integer code;
	private String message;
	@JsonRawValue
	private Object originalMessage;
	private List<ValidationDto> validationDetails;
	
	@Override
	public String toString() {
		return new GsonBuilder()
				.setLenient()
				.setPrettyPrinting()
				.enableComplexMapKeySerialization()
				.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.create().toJson(this, ErrorResponseDto.class);
	}
}
