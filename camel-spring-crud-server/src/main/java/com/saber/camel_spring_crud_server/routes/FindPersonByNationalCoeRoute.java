package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FindPersonByNationalCoeRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findByNationalCode/{nationalCode}")
				.id(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
				.description("find Person by nationalCode")
				.produces(MediaType.APPLICATION_JSON_VALUE)
				.enableCORS(true)
				.param().name(Headers.nationalCode).type(RestParamType.header).example("0079028748").dataType("string").required(true).endParam()
				.responseMessage().code(200).responseModel(FindAllPersonResponse.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.responseMessage().code(400).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(401).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(403).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(404).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(500).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.responseMessage().code(504).responseModel(ServiceErrorResponse.class).endResponseMessage()
				.bindingMode(RestBindingMode.off)
				.route()
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));
		
		from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.log("Request for find person by nationalCode ===> ${in.header.nationalCode}")
				.toD("sql:select * from persons  where nationalCode=${in.header.nationalCode}");
		
		from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.marshal().json(JsonLibrary.Jackson)
				.log("Response find person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}")
				.choice()
					.when(body().isEqualTo("[ ]"))
					.to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
				.otherwise()
					.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
				.end();
		
		from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.process(exchange -> {
					String response = exchange.getIn().getBody(String.class);
					response = String.format("{\"persons\":%s}", response.trim().toLowerCase());
					exchange.getIn().setBody(response);
				})
				.unmarshal().json(JsonLibrary.Jackson, FindAllPersonResponse.class)
				.marshal().json(JsonLibrary.Jackson, FindAllPersonResponse.class)
				.log("Response find person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
		
	}
}
