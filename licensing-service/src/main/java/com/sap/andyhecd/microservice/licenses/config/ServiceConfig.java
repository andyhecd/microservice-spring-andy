package com.sap.andyhecd.microservice.licenses.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig{
	
	@Bean
    @ConfigurationProperties(prefix = "example")
    public ExampleProps exampleProps() {
        return new ExampleProps();
    }

}
