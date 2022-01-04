package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
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
				.responseMessage().code(200).responseModel(FindAllPersonResponse.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.responseMessage().code(400).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(401).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(403).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(404).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(500).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(504).responseModel(ServiceErrorResponse.class).endResponseMessage()
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
				.to("sql:select * from persons")
				.marshal().json(JsonLibrary.Jackson)
				.process(exchange -> {
					String response = exchange.getIn().getBody(String.class);
					response = String.format("{\"persons\":%s}", response.trim().toLowerCase());
					exchange.getIn().setBody(response);
				})
				.unmarshal().json(JsonLibrary.Jackson, FindAllPersonResponse.class)
				.marshal().json(JsonLibrary.Jackson, FindAllPersonResponse.class)
				.log("Response for find All persons ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
	}
}
