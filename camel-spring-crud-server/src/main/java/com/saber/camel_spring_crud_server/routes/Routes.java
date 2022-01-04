package com.saber.camel_spring_crud_server.routes;

public interface Routes {
	
	
	String SAY_HELLO_ROUTE = "say-hello-route";
	String FIND_ALL_PERSON_ROUTE = "find-all-person-route";
	String ADD_PERSON_ROUTE = "add-person-route";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE = "find-person-by-national-code-route" ;
	
	//////////////////////////////////////////////////////////////////
	String SAY_HELLO_ROUTE_GATEWAY = "say-hello-route-gateway";
	String FIND_ALL_PERSON_ROUTE_GATEWAY = "find-all-person-route-gateway";
	String ADD_PERSON_ROUTE_GATEWAY = "add-person-route-gateway";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY = "find-person-by-national-code-route-gateway" ;
	
	////////////////////////////////////////////////////
	String SAY_HELLO_ROUTE_GATEWAY_OUT = "say-hello-route-gateway-out";
	String FIND_ALL_PERSON_ROUTE_GATEWAY_OUT = "find-all-person-route-gateway-out";
	String ADD_PERSON_ROUTE_GATEWAY_OUT = "add-person-route-gateway-out";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT = "find-person-by-national-code-route-gateway-out" ;
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT_RESULT = "find-person-by-national-code-route-gateway-out-result" ;
	
	///////////////////////////////////////////////////////////////
	String SAY_HELLO_ROUTE_GROUP = "say-hello-route-group";
	String FIND_ALL_PERSON_ROUTE_GROUP = "find-all-person-route-group";
	String ADD_PERSON_ROUTE_GROUP = "add-person-route-group";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP = "find-person-by-national-code-route-group" ;
	String THROWS_EXCEPTION_ROUTE_GROUP = "throws-exception-route-group";
	
	//////////////////////////////////////////////////////////////
	String SAY_HELLO_ROUTE_ERROR_HANDLER = "say-hello-route-error-handler";
	String SAY_HELLO_ROUTE_FINALLY = "say-hello-route-finally";
	String JSON_MAPPING_EXCEPTION_ROUTE = "json-mapping-exception-route";
	String JSON_PARSE_EXCEPTION_ROUTE = "json-parse-exception-route";
	String BEAN_VALIDATION_EXCEPTION_ROUTE = "bean-validation-exception-route";
	String RESOURCE_DUPLICATION_EXCEPTION_ROUTE = "resource-duplication-exception-route";
	String THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE = "throws-resource-duplication-exception-route";
	String THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE = "throws-resource-notfound-exception-route";
	String EXCEPTION_HANDLER_ROUTE_GROUP = "exception-handler-route-group";
	String RESOURCE_NOTFOUND_EXCEPTION_ROUTE = "resource-notfound-exception-route";
}
