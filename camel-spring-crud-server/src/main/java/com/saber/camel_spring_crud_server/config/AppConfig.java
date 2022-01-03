package com.saber.camel_spring_crud_server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.saber.camel_spring_crud_server.routes.Headers;
import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
							.end();
				}
			}
		};
	}
	
}
