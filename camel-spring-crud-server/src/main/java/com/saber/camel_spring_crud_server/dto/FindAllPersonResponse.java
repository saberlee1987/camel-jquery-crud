package com.saber.camel_spring_crud_server.dto;

import lombok.Data;
import java.util.List;

@Data
public class FindAllPersonResponse {
	private List<PersonDto> persons;
}