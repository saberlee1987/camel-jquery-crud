package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.FindAllPersonResponse;
import com.saber.camel_spring_crud_server.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.HttpStatus;
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
                .responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(FindAllPersonResponse.class).example("example1", "{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
                .responseMessage().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.NOT_ACCEPTABLE.value()).message(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(HttpStatus.GATEWAY_TIMEOUT.value()).message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase()).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Request for find person by nationalCode ===> ${in.header.nationalCode}")
                .toD("sql:select * from persons  where nationalCode=${in.header.nationalCode}?outputType=selectOne&outputClass=com.saber.camel_spring_crud_server.dto.PersonDto");

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Response find person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}")
                .choice()
                  .when(body().isNull())
                        .to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
                  .otherwise()
                    .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
                .end()
                .log("Response find person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}");

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
