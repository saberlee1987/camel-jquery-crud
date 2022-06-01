package com.saber.camel_spring_crud_server.routes;

import com.saber.camel_spring_crud_server.dto.PersonDto;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindPersonByNationalCoeRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        rest("/persons")
                .get("/findByNationalCode/{nationalCode}")
                .id(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
                .description("find Person by nationalCode")
                .param().name(Headers.nationalCode).type(RestParamType.header).example("0079028748").dataType("string").required(true).endParam()
                .responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(PersonDto.class).example("example1", "{\"firstname\": \"saber\",\"lastname\": \"azizi\", \"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09124567895\"}").endResponseMessage()
                .route()
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .setHeader(Headers.url,simple("{{service.api.base-path}}/persons/findByNationalCode/${in.header.nationalCode}"))
                .setHeader(Headers.correlation,constant(UUID.randomUUID().toString()))
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Request for ${in.header.url} , correlation : ${in.header.correlation} find person by nationalCode ===> ${in.header.nationalCode}")
                .toD("sql:select * from persons  where nationalCode=${in.header.nationalCode}?outputType=selectOne&outputClass="+PersonDto.class.getName());

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Response for ${in.header.url} , correlation : ${in.header.correlation} find  person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}")
                .choice()
                  .when(body().isNull())
                        .to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
                  .otherwise()
                    .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
                .end()
                .log("Response for ${in.header.url} , correlation : ${in.header.correlation} find person by nationalCode ===> ${in.header.nationalCode} ===> ${in.body}");

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
