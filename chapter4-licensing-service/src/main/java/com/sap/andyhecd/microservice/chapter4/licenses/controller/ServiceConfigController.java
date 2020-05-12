package com.sap.andyhecd.microservice.chapter4.licenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.andyhecd.microservice.chapter4.licenses.config.ExampleProps;

@RestController
public class ServiceConfigController {
	
	@Autowired
    ExampleProps exampleProps;

	@RequestMapping(value = "/examplestring", method = RequestMethod.GET)
	public String updateLicenses() {
		return String.format(exampleProps.getProperty());
	}

}
