package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import com.saber.camel_spring_crud_server.dto.ServiceResponseEnum;
import com.saber.camel_spring_crud_server.dto.ValidationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class ExceptionResponseRoute extends RouteBuilder {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void configure() throws Exception {
		
		from(String.format("direct:%s", Routes.JSON_MAPPING_EXCEPTION_ROUTE))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.process(exchange -> {
					JsonMappingException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, JsonMappingException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getCode());
					errorResponse.setMessage(ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getMessage());
					errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getCode(), exception.getOriginalMessage()));
					ValidationDto validationDto = new ValidationDto();
					validationDto.setFieldName(exception.getPath().toString());
					validationDto.setDetailMessage(exception.getLocalizedMessage());
					errorResponse.setValidationDetails(Collections.singletonList(validationDto));
					log.error("Error JsonMappingException ===> {}", mapper.writeValueAsString(errorResponse));
					
				})
				.marshal().json(JsonLibrary.Jackson);
		
		from(String.format("direct:%s", Routes.JSON_PARSE_EXCEPTION_ROUTE))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.process(exchange -> {
					JsonParseException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, JsonParseException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getCode());
					errorResponse.setMessage(ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getMessage());
					errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", ServiceResponseEnum.JSON_MAPPING_EXCEPTION.getCode(), exception.getOriginalMessage()));
					ValidationDto validationDto = new ValidationDto();
					validationDto.setFieldName(exception.getRequestPayloadAsString());
					validationDto.setDetailMessage(exception.getLocalizedMessage());
					errorResponse.setValidationDetails(Collections.singletonList(validationDto));
					log.error("Error JsonMappingException ===> {}", mapper.writeValueAsString(errorResponse));
					
				})
				.marshal().json(JsonLibrary.Jackson);
	}
}
