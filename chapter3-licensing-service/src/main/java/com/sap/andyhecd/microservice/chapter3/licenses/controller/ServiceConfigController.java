package com.sap.andyhecd.microservice.chapter3.licenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.andyhecd.microservice.chapter3.licenses.config.ServiceConfig;

@RestController
public class ServiceConfigController {
	
	@Autowired
	ServiceConfig serviceConfig;

	@RequestMapping(value = "/examplestring", method = RequestMethod.GET)
	public String updateLicenses() {
		return String.format(serviceConfig.getExampleProperty());
	}

}
