package com.sap.andyhecd.microservice.licenses.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sap.andyhecd.microservice.licenses.model.License;
import com.sap.andyhecd.microservice.licenses.service.LicenseService;
import com.sap.andyhecd.microservice.licenses.util.UserContextHolder;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {
	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceController.class);
	@Autowired
	private LicenseService licenseService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
		logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return licenseService.getLicensesByOrg(organizationId);
	}

	@RequestMapping(value = "/sleep/{sleppInMilliseconds}", method = RequestMethod.GET)
	public List<License> getLicensesWithSleep(@PathVariable("organizationId") String organizationId,
			@PathVariable("sleppInMilliseconds") int sleppInMilliseconds) {
		logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return licenseService.getLicensesByOrgWithSleep(organizationId, sleppInMilliseconds);
	}

	@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public License getLicenses(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		
		return licenseService.getLicense(organizationId, licenseId, "");
	}

	@RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
	public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId, @PathVariable("clientType") String clientType) {

		return licenseService.getLicense(organizationId, licenseId, clientType);
	}

	@RequestMapping(value = "{licenseId}", method = RequestMethod.PUT)
	public void updateLicenses(@PathVariable("licenseId") String licenseId, @RequestBody License license) {
		licenseService.updateLicense(license);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void saveLicenses(@RequestBody License license) {
		licenseService.saveLicense(license);
	}

	@RequestMapping(value = "{licenseId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLicenses(@PathVariable("licenseId") String licenseId, @RequestBody License license) {
		licenseService.deleteLicense(license);
	}
}