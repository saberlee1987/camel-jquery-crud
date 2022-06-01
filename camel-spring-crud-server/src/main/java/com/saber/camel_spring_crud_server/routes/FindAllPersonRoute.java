package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import com.saber.camel_spring_crud_server.dto.PersonDto;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class FindAllPersonRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findAll")
				.id(Routes.FIND_ALL_PERSON_ROUTE)
				.description("find All Persons")
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(FindAllPersonResponse.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.route()
				.routeId(Routes.FIND_ALL_PERSON_ROUTE)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.setHeader(Headers.url,simple("{{service.api.base-path}}/persons/findAll"))
				.setHeader(Headers.correlation,constant(UUID.randomUUID().toString()))
				.to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.log("Request for ${in.header.url} , correlation : ${in.header.correlation} find all Persons ")
				.to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT));
		
		from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT))
				.routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.to("sql:select * from persons?outputClass="+ PersonDto.class.getName())
				.log("Response for ${in.header.url} , correlation : ${in.header.correlation} find All persons ===> ${in.body}")
				.marshal().json(JsonLibrary.Jackson)
				.log("Response for ${in.header.url} , correlation : ${in.header.correlation} find All persons ===> ${in.body}")
				.process(exchange -> {
					String response = exchange.getIn().getBody(String.class);
					response = String.format("{\"persons\":%s}", response.trim());
					exchange.getIn().setBody(response);
				})
				.log("Response for ${in.header.url} , correlation : ${in.header.correlation} find All persons ===> ${in.body}")
				.unmarshal().json(JsonLibrary.Jackson)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
	}
}
