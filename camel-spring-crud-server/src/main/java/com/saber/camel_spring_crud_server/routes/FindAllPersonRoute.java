package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class FindAllPersonRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findAll")
				.id(Routes.FIND_ALL_PERSON_ROUTE)
				.description("find All Persons")
				.produces(MediaType.APPLICATION_JSON_VALUE)
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(FindAllPersonResponse.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.responseMessage().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.NOT_ACCEPTABLE.value()).message(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(HttpStatus.GATEWAY_TIMEOUT.value()).message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.enableCORS(true)
				.bindingMode(RestBindingMode.off)
				.route()
				.routeId(Routes.FIND_ALL_PERSON_ROUTE)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.log("Request for find all Persons ")
				.to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT));
		
		from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT))
				.routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.to("sql:select * from persons?outputClass=com.saber.camel_spring_crud_server.dto.PersonDto")
				.log("Response for find All persons ===> ${in.body}")
				.marshal().json(JsonLibrary.Jackson)
				.log("Response for find All persons ===> ${in.body}")
				.process(exchange -> {
					String response = exchange.getIn().getBody(String.class);
					response = String.format("{\"persons\":%s}", response.trim().toLowerCase());
					exchange.getIn().setBody(response);
				})
				.log("Response for find All persons ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
	}
}
