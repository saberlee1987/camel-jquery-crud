package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.dto.PersonDto;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import com.saber.camel_spring_crud_server.dto.UpdatePersonResponseDto;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpdatePersonRoute extends AbstractRestRouteBuilder {

	private ObjectMapper mapper;

	public UpdatePersonRoute(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.put("/update/{nationalCode}")
				.id(Routes.UPDATE_PERSON_ROUTE)
				.description("update person")
				.produces(MediaType.APPLICATION_JSON_VALUE)
				.consumes(MediaType.APPLICATION_JSON_VALUE)
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(UpdatePersonResponseDto.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.responseMessage().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_ACCEPTABLE.value()).message(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.GATEWAY_TIMEOUT.value()).message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.param().name("nationalCode").type(RestParamType.header).dataType("string").example("0079028748").required(true).endParam()
				.bindingMode(RestBindingMode.json)
				.enableCORS(true)
				.type(PersonDto.class)
				.route()
				.routeId(Routes.UPDATE_PERSON_ROUTE)
				.routeGroup(Routes.UPDATE_PERSON_ROUTE_GROUP)
				.to("bean-validator://update-person-route")
				.to(String.format("direct:%s", Routes.UPDATE_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.UPDATE_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.UPDATE_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.UPDATE_PERSON_ROUTE_GROUP)
				.setHeader(Headers.requestBody, simple("${in.body}"))
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.log("Response for find Person by nationalCode ${in.header.nationalCode} ===> ${in.body}")
				.choice()
					.when(body().isNull())
					.to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
				.otherwise()
					.to(String.format("direct:%s", Routes.UPDATE_PERSON_ROUTE_GATEWAY_OUT))
				.end();

		from(String.format("direct:%s", Routes.UPDATE_PERSON_ROUTE_GATEWAY_OUT))
				.routeId(Routes.UPDATE_PERSON_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.UPDATE_PERSON_ROUTE_GROUP)
				.process(exchange -> {
					PersonDto personDto = exchange.getIn().getHeader(Headers.requestBody, PersonDto.class);
					Map<String, Object> map = new HashMap<>();
					map.put("firstname", personDto.getFirstname());
					map.put("lastname", personDto.getLastname());
					map.put("age", personDto.getAge());
					map.put("email", personDto.getEmail());
					map.put("mobile", personDto.getMobile());
					exchange.getIn().setBody(map);
				})
				.toD("sql:update persons set firstname=:#firstname, lastname=:#lastname, age=:#age, email=:#email, mobile=:#mobile where nationalCode=${in.header.nationalCode}")
				.process(exchange -> {
					UpdatePersonResponseDto responseDto = new UpdatePersonResponseDto();
					responseDto.setCode(0);
					responseDto.setMessage("Person updated to table successfully");
					log.info("Response for updated person for nationalCode ==> {}  , body ===> {}",exchange.getIn().getHeader(Headers.nationalCode),mapper.writeValueAsString(responseDto));
					exchange.getIn().setBody(responseDto);
				})
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

	

	}
}
