package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.AddPersonResponseDto;
import com.saber.camel_spring_crud_server.dto.PersonDto;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AddPersonRoute extends AbstractRestRouteBuilder {
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.post("/add")
				.id(Routes.ADD_PERSON_ROUTE)
				.description("add person")
				.produces(MediaType.APPLICATION_JSON_VALUE)
				.consumes(MediaType.APPLICATION_JSON_VALUE)
				.responseMessage().code(200).responseModel(AddPersonResponseDto.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.responseMessage().code(400).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(401).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(403).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(404).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(500).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(504).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.bindingMode(RestBindingMode.json)
				.enableCORS(true)
				.type(PersonDto.class)
				.route()
				.routeId(Routes.ADD_PERSON_ROUTE)
				.routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
				.to("bean-validator://add-person-route")
				.to(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.ADD_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
				.setHeader(Headers.nationalCode, simple(String.format("${in.body.%s}", Headers.nationalCode)))
				.setHeader(Headers.requestBody, simple("${in.body}"))
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.marshal().json(JsonLibrary.Jackson)
				.log("Response for find Person by nationalCode ${in.header.nationalCode} ===> ${in.body}")
				.choice()
					.when(body().isNotEqualTo("[ ]"))
					.to(String.format("direct:%s", Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE))
				.otherwise()
					.to(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY_OUT))
				.end();

		from(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY_OUT))
				.routeId(Routes.ADD_PERSON_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
				.process(exchange -> {
					PersonDto personDto = exchange.getIn().getHeader(Headers.requestBody, PersonDto.class);
					Map<String, Object> map = new HashMap<>();
					map.put("firstname", personDto.getFirstname());
					map.put("lastname", personDto.getLastname());
					map.put("nationalCode", personDto.getNationalCode());
					map.put("age", personDto.getAge());
					map.put("email", personDto.getEmail());
					map.put("mobile", personDto.getMobile());
					exchange.getIn().setBody(map);
				})
				.to("sql:insert into persons (firstname, lastname, nationalCode, age, email, mobile) values (:#firstname,:#lastname,:#nationalCode,:#age,:#email,:#mobile)")
				.process(exchange -> {
					AddPersonResponseDto personResponseDto = new AddPersonResponseDto();
					personResponseDto.setCode(0);
					personResponseDto.setMessage("Person add to table successfully");
					exchange.getIn().setBody(personResponseDto);
				})
				.log("Response for add person for nationalCode ${in.header.nationalCode} ====> ${in.header.nationalCode} , body ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

	

	}
}