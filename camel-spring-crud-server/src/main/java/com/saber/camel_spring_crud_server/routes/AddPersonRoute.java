package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.dto.AddPersonResponseDto;
import com.saber.camel_spring_crud_server.dto.PersonDto;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AddPersonRoute extends AbstractRestRouteBuilder {

	private final ObjectMapper mapper;

	public AddPersonRoute(ObjectMapper mapper) {
		this.mapper = mapper;
	}


	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.post("/add")
				.id(Routes.ADD_PERSON_ROUTE)
				.description("add person")
				.consumes(MediaType.APPLICATION_JSON_VALUE)
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(AddPersonResponseDto.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.type(PersonDto.class)
				.route()
				.routeId(Routes.ADD_PERSON_ROUTE)
				.routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
				.setHeader(Headers.url,simple("{{service.api.base-path}}/persons/add"))
				.setHeader(Headers.correlation,constant(UUID.randomUUID().toString()))
				.to("bean-validator://add-person-route")
				.to(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.ADD_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
				.log("Request for ${in.header.url} , correlation : ${in.header.correlation} with body ${in.body}")
				.setHeader(Headers.nationalCode, simple(String.format("${in.body.%s}", Headers.nationalCode)))
				.setHeader(Headers.requestBody, simple("${in.body}"))
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.log("Response for ${in.header.url} , correlation : ${in.header.correlation} find Person by nationalCode ${in.header.nationalCode} ===> ${in.body}")
				.choice()
					.when(body().isNotNull())
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
					log.info("Response for {} , correlation : {} add person for nationalCode ===>  {} , body ===> {}"
							,exchange.getIn().getHeader(Headers.url)
							,exchange.getIn().getHeader(Headers.correlation)
							,exchange.getIn().getHeader(Headers.nationalCode)
							,mapper.writeValueAsString(personResponseDto));
					exchange.getIn().setBody(personResponseDto);
				})
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

	

	}
}
