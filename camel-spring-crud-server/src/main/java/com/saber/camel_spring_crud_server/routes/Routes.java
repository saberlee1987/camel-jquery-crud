package com.saber.camel_spring_crud_server.routes;

public interface Routes {
	String SAY_HELLO_ROUTE = "say-hello-route";
	String FIND_ALL_PERSON_ROUTE = "find-all-person-route";
	
	String SAY_HELLO_ROUTE_GATEWAY = "say-hello-route-gateway";
	String FIND_ALL_PERSON_ROUTE_GATEWAY = "find-all-person-route-gateway";
	
	String SAY_HELLO_ROUTE_GATEWAY_OUT = "say-hello-route-gateway-out";
	String FIND_ALL_PERSON_ROUTE_GATEWAY_OUT = "find-all-person-route-gateway-out";
	
	
	String SAY_HELLO_ROUTE_GROUP = "say-hello-route-group";
	String FIND_ALL_PERSON_ROUTE_GROUP = "find-all-person-route-group";
	
	
	String SAY_HELLO_ROUTE_ERROR_HANDLER = "say-hello-route-error-handler";
	String SAY_HELLO_ROUTE_FINALLY = "say-hello-route-finally";
	String JSON_MAPPING_EXCEPTION_ROUTE = "json-mapping-exception-route";
	String JSON_PARSE_EXCEPTION_ROUTE = "json-parse-exception-route";
}
