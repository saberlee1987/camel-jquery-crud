package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.saber.camel_spring_crud_server.exceptions.ResourceDuplicationException;
import com.saber.camel_spring_crud_server.exceptions.ResourceNotFoundException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;

public class AbstractRestRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
	
		onException(JsonMappingException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.to(String.format("direct:%s",Routes.JSON_MAPPING_EXCEPTION_ROUTE));
		
		onException(JsonParseException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.to(String.format("direct:%s",Routes.JSON_PARSE_EXCEPTION_ROUTE));
		
		onException(BeanValidationException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.to(String.format("direct:%s",Routes.BEAN_VALIDATION_EXCEPTION_ROUTE));
		
		onException(ResourceDuplicationException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.to(String.format("direct:%s",Routes.RESOURCE_DUPLICATION_EXCEPTION_ROUTE));
		
		onException(ResourceNotFoundException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.to(String.format("direct:%s",Routes.RESOURCE_NOTFOUND_EXCEPTION_ROUTE));
		
		from(String.format("direct:%s", Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE))
				.routeId( Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE)
				.routeGroup(Routes.THROWS_EXCEPTION_ROUTE_GROUP)
				.process(exchange -> {
					String nationalCode= exchange.getIn().getHeader(Headers.nationalCode,String.class);
					throw new ResourceDuplicationException(String.format("person with nationalCode %s already exist",nationalCode));
				});
		
		from(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
				.routeId( Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE)
				.routeGroup(Routes.THROWS_EXCEPTION_ROUTE_GROUP)
				.process(exchange -> {
					String nationalCode= exchange.getIn().getHeader(Headers.nationalCode,String.class);
					throw new ResourceNotFoundException(String.format("person with nationalCode %s does not exist",nationalCode));
				});
		
	}
}
