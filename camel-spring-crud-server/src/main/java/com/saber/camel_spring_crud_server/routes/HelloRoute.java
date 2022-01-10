package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.beans.SayHelloBean;
import com.saber.camel_spring_crud_server.dto.HelloResponseDto;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import com.saber.camel_spring_crud_server.dto.ValidationDto;
import com.saber.camel_spring_crud_server.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class HelloRoute extends AbstractRestRouteBuilder {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/hello")
				.get("/sayHello")
				.id(Routes.SAY_HELLO_ROUTE)
				.description("say Hello")
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(HelloResponseDto.class).endResponseMessage()
				.responseMessage().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_ACCEPTABLE.value()).message(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.GATEWAY_TIMEOUT.value()).message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.produces(MediaType.APPLICATION_JSON_VALUE)
				.param().name("firstName").type(RestParamType.query).dataType("string").required(true).example("Saber").endParam()
				.param().name("lastName").type(RestParamType.query).dataType("string").required(true).example("Azizi").endParam()
				.enableCORS(true)
				.bindingMode(RestBindingMode.json)
				.route()
				.routeId(Routes.SAY_HELLO_ROUTE)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.to(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY))
				.routeId(Routes.SAY_HELLO_ROUTE_GATEWAY)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.log("Request for sayHello { firstName : ${in.header.firstName} , lastName : ${in.header.lastName}")
				.doTry()
					.bean(SayHelloBean.class, "sayHello")
					.to(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY_OUT))
				.doCatch(BadRequestException.class)
					.to(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_ERROR_HANDLER))
				.doFinally()
					.to(String.format("direct:%s",Routes.SAY_HELLO_ROUTE_FINALLY))
				.endDoTry();
		
		from(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY_OUT))
				.routeId(Routes.SAY_HELLO_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.log("Response for sayHello ===>  ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
		
		from(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_ERROR_HANDLER))
				.routeId(Routes.SAY_HELLO_ROUTE_ERROR_HANDLER)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.process(exchange -> {
					BadRequestException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BadRequestException.class);
					ServiceErrorResponse errorResponse = new ServiceErrorResponse();
					errorResponse.setCode(4);
					errorResponse.setMessage("BadRequestException");
					ValidationDto validationDto = new ValidationDto();
					validationDto.setFieldName(exception.getFieldName());
					validationDto.setDetailMessage(exception.getDetailMessage());
					errorResponse.setValidationDetails(Collections.singletonList(validationDto));
					log.error("Error BadRequestException ===> {}", mapper.writeValueAsString(errorResponse));
					exchange.getIn().setBody(errorResponse);
				})
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400));
		
		from(String.format("direct:%s",Routes.SAY_HELLO_ROUTE_FINALLY))
				.routeId(Routes.SAY_HELLO_ROUTE_FINALLY)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.log("finally block log .................................................");
		
		
	}
}
