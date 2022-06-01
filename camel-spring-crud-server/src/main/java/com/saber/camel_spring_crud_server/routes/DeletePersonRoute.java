package com.saber.camel_spring_crud_server.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.camel_spring_crud_server.dto.DeletePersonResponseDto;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeletePersonRoute extends AbstractRestRouteBuilder {

	private ObjectMapper mapper;

	public DeletePersonRoute(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.delete("/delete/{nationalCode}")
				.id(Routes.DELETE_PERSON_ROUTE)
				.description("delete person")
				.consumes(MediaType.APPLICATION_JSON_VALUE)
				.responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(DeletePersonResponseDto.class).example("example1","{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
				.param().name("nationalCode").type(RestParamType.header).dataType("string").example("0079028748").required(true).endParam()
				.route()
				.routeId(Routes.DELETE_PERSON_ROUTE)
				.routeGroup(Routes.DELETE_PERSON_ROUTE_GROUP)
				.setHeader(Headers.url,simple("{{service.api.base-path}}/persons/delete/${in.header.nationalCode}"))
				.setHeader(Headers.correlation,constant(UUID.randomUUID().toString()))
				.to(String.format("direct:%s", Routes.DELETE_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.DELETE_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.DELETE_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.DELETE_PERSON_ROUTE_GROUP)
				.log("Request for ${in.header.url} , correlation : ${in.header.correlation} with nationalCode ${in.header.nationalCode}")
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.log("Response for ${in.header.url} , correlation : ${in.header.correlation} with nationalCode ${in.header.nationalCode} ===> ${in.body}")
				.choice()
					.when(body().isNull())
					.to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
				.otherwise()
					.to(String.format("direct:%s", Routes.DELETE_PERSON_ROUTE_GATEWAY_OUT))
				.end();

		from(String.format("direct:%s", Routes.DELETE_PERSON_ROUTE_GATEWAY_OUT))
				.routeId(Routes.DELETE_PERSON_ROUTE_GATEWAY_OUT)
				.routeGroup(Routes.DELETE_PERSON_ROUTE_GROUP)
				.toD("sql:delete from persons where nationalCode=${in.header.nationalCode}")
				.process(exchange -> {
					DeletePersonResponseDto responseDto = new DeletePersonResponseDto();
					responseDto.setCode(0);
					responseDto.setMessage("Person deleted to table successfully");
					log.info("Response for {} , correlation : {} for nationalCode ==> {}  , body ===> {}"
							,exchange.getIn().getHeader(Headers.url)
							,exchange.getIn().getHeader(Headers.correlation)
							,exchange.getIn().getHeader(Headers.nationalCode)
							,mapper.writeValueAsString(responseDto));
					exchange.getIn().setBody(responseDto);
				})
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

	

	}
}
