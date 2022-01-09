package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import com.saber.camel_spring_crud_server.dto.ServiceResponseEnum;
import com.saber.camel_spring_crud_server.dto.ValidationDto;
import com.saber.camel_spring_crud_server.exceptions.ResourceDuplicationException;
import com.saber.camel_spring_crud_server.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ExceptionResponseRoute extends RouteBuilder {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void configure() throws Exception {
		
		from(String.format("direct:%s", Routes.JSON_MAPPING_EXCEPTION_ROUTE))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.routeId(Routes.JSON_MAPPING_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
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
					exchange.getMessage().setBody(errorResponse);
				});
		
		from(String.format("direct:%s", Routes.JSON_PARSE_EXCEPTION_ROUTE))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.routeId(Routes.JSON_PARSE_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
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
					exchange.getMessage().setBody(errorResponse);
				});
		
		
		from(String.format("direct:%s", Routes.RESOURCE_DUPLICATION_EXCEPTION_ROUTE))
				.routeId(Routes.RESOURCE_DUPLICATION_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.process(exchange -> {
					ResourceDuplicationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ResourceDuplicationException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(ServiceResponseEnum.RESOURCE_DUPLICATION_EXCEPTION.getCode());
					errorResponse.setMessage(ServiceResponseEnum.RESOURCE_DUPLICATION_EXCEPTION.getMessage());
					errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", ServiceResponseEnum.RESOURCE_DUPLICATION_EXCEPTION.getCode(), exception.getMessage()));
					log.error("Error ResourceDuplicationException ===> {}", mapper.writeValueAsString(errorResponse));
					
					exchange.getMessage().setBody(errorResponse);
				});
		
		from(String.format("direct:%s", Routes.RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
				.routeId(Routes.RESOURCE_NOTFOUND_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406))
				.process(exchange -> {
					ResourceNotFoundException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ResourceNotFoundException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(ServiceResponseEnum.RESOURCE_NOT_FOUND_EXCEPTION.getCode());
					errorResponse.setMessage(ServiceResponseEnum.RESOURCE_NOT_FOUND_EXCEPTION.getMessage());
					errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", ServiceResponseEnum.RESOURCE_NOT_FOUND_EXCEPTION.getCode(), exception.getMessage()));
					log.error("Error ResourceNotFoundException ===> {}", mapper.writeValueAsString(errorResponse));
					exchange.getMessage().setBody(errorResponse);
				});
		
		
		from(String.format("direct:%s",Routes.BEAN_VALIDATION_EXCEPTION_ROUTE))
				.routeId(Routes.BEAN_VALIDATION_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
				.process(exchange -> {
					BeanValidationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BeanValidationException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(ServiceResponseEnum.BEAN_VALIDATION_EXCEPTION.getCode());
					errorResponse.setMessage(ServiceResponseEnum.BEAN_VALIDATION_EXCEPTION.getMessage());
					
					List<ValidationDto> validationDetails = new ArrayList<>();
					
					for (ConstraintViolation<Object> constraintViolation : exception.getConstraintViolations()) {
						ValidationDto validationDto = new ValidationDto();
						validationDto.setFieldName(constraintViolation.getPropertyPath().toString());
						validationDto.setDetailMessage(constraintViolation.getMessage());
						validationDetails.add(validationDto);
					}
					errorResponse.setValidationDetails(validationDetails);
					log.error("Error BeanValidationException ===> {}", mapper.writeValueAsString(errorResponse));
					exchange.getMessage().setBody(errorResponse);
				});
	}
}
