package com.saber.camel_spring_crud_server.dto;

import lombok.Data;

@Data
public class ValidationDto {
	private String fieldName;
	private String detailMessage;
}
