package com.saber.camel_spring_crud_server.dto;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

@Data
public class AddPersonResponseDto {
	private Integer code;
	private String message;
	
	@Override
	public String toString() {
		return new GsonBuilder()
				.setLenient()
				.setPrettyPrinting()
				.enableComplexMapKeySerialization()
				.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.create().toJson(this, AddPersonResponseDto.class);
	}
}
