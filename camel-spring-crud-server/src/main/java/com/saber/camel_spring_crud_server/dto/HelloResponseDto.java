package com.saber.camel_spring_crud_server.dto;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class HelloResponseDto {
	private String message;
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("message", message)
				.toString();
	}
}
