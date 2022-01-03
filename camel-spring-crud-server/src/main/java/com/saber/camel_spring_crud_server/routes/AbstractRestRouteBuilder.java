package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
@Slf4j
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
		
	}
}
