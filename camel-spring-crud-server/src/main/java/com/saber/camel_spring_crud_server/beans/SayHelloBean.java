package com.saber.camel_spring_crud_server.beans;

import com.saber.camel_spring_crud_server.dto.HelloResponseDto;
import com.saber.camel_spring_crud_server.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component(value = "sayHello")
@Slf4j
public class SayHelloBean {
	
	@Handler
	public HelloResponseDto sayHello(@Header(value = "firstName") String firstName, @Header(value = "lastName") String lastName) {
		if (firstName == null) {
			throw new BadRequestException("firstName", "firstName is Required");
		} else if (firstName.trim().length() < 3) {
			throw new BadRequestException("firstName", "firstName must be > 3 character");
		} else {
			log.info("firstName is ====> {}", firstName);
		}
		
		if (lastName == null) {
			throw new BadRequestException("lastName", "lastName is Required");
		} else if (lastName.trim().length() < 3) {
			throw new BadRequestException("lastName", "lastName must be > 3 character");
		} else {
			log.info("lastName is ====> {}", lastName);
		}
		
		String message = String.format("Hello %s %s", firstName, lastName);
		
		HelloResponseDto helloResponseDto = new HelloResponseDto();
		helloResponseDto.setMessage(message);
		return helloResponseDto;
	}
}
