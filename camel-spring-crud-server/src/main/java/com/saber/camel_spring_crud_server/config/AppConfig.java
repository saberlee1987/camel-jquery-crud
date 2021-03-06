package com.saber.camel_spring_crud_server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.saber.camel_spring_crud_server.dto.ErrorResponseDto;
import com.saber.camel_spring_crud_server.routes.Headers;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.actuate.metrics.web.servlet.DefaultWebMvcTagsProvider;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AppConfig {
	
	
	@Bean(value = "mapper")
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		return mapper;
	}
	
	@Bean
	public CamelContextConfiguration camelConfig() {
		return new CamelContextConfiguration() {
			@Override
			public void beforeApplicationStart(CamelContext camelContext) {
				
				camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
				camelContext.addRoutePolicyFactory(new MetricsRoutePolicyFactory());
				camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());
			}
			
			@Override
			public void afterApplicationStart(CamelContext camelContext) {
				SpringCamelContext springCamelContext = (SpringCamelContext) camelContext;
				for (RestDefinition restDefinition : springCamelContext.getRestDefinitions()) {
					restDefinition.
							security(Headers.Authorization)
							.securityDefinitions()
							.apiKey(Headers.Authorization, "Authorization Header").withHeader(Headers.Authorization)
							.end()
							
									.end()
							.produces(MediaType.APPLICATION_JSON_VALUE)
							.bindingMode(RestBindingMode.json)
							.enableCORS(true)
							
							.responseMessage().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.NOT_ACCEPTABLE.value()).message(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.SERVICE_UNAVAILABLE.value()).message(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
							.responseMessage().code(HttpStatus.GATEWAY_TIMEOUT.value()).message(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase()).responseModel(ErrorResponseDto.class).endResponseMessage()
					;
				}
			}
		};
	}
	
	@Bean
	public WebMvcTagsProvider webMvcTagsProvider() {
		return new DefaultWebMvcTagsProvider() {
			@Override
			public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable exception) {
				return Tags.concat(super.getTags(request, response, handler, exception),
						Tags.of("uri", request.getRequestURI())
				);
			}
		};
	}
	
}
