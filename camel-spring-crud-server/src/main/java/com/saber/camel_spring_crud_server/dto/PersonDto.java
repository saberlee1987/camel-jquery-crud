package com.saber.camel_spring_crud_server.dto;

import lombok.Data;

@Data
public class PersonDto {
	private String firstname;
	private String lastname;
	private Integer age;
	private String email;
	private String mobile;
}