package com.saber.camel_spring_crud_server.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteDefinition extends RouteBuilder {

	@Value(value = "${service.api.base-path}")
	private String apiBasePath;
	@Value(value = "${service.swagger.path}")
	private String swaggerPath;
	@Value(value = "${service.swagger.title}")
	private String swaggerTitle;
	@Value(value = "${service.swagger.version}")
	private String swaggerVersion;
	@Value(value = "${service.log.pretty-print}")
	private String prettyPrint;
	
	
	@Override
	public void configure() throws Exception {
		
		restConfiguration()
				.contextPath(apiBasePath)
				.apiContextPath(swaggerPath)
				.apiContextRouteId("api-doc")
				.enableCORS(true)
				.corsHeaderProperty("Access-Control-Allow-Headers","*")
				.corsHeaderProperty("Access-Control-Allow-Methods","GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH ")
				.corsHeaderProperty("Access-Control-Allow-Origin","*")
				.corsHeaderProperty("Access-Control-Max-Age","3000")
				.bindingMode(RestBindingMode.json)
				.apiProperty("api.title",swaggerTitle)
				.apiProperty("api.version",swaggerVersion)
				.apiProperty("cors","true")
				.component("servlet")
				.dataFormatProperty("prettyPrint",prettyPrint);
	}
}