package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public class FindAllPersonRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findAll")
				.id(Routes.FIND_ALL_PERSON_ROUTE)
				.description("find All Persons")
				.produces(MediaType.APPLICATION_JSON)
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
