package com.sap.andyhecd.microservice.chapter4.licenses.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.andyhecd.microservice.chapter4.licenses.client.OrganizationDiscoveryClient;
import com.sap.andyhecd.microservice.chapter4.licenses.client.OrganizationFeignClient;
import com.sap.andyhecd.microservice.chapter4.licenses.client.OrganizationRestTemplateClient;
import com.sap.andyhecd.microservice.chapter4.licenses.config.ExampleProps;
import com.sap.andyhecd.microservice.chapter4.licenses.model.License;
import com.sap.andyhecd.microservice.chapter4.licenses.model.Organization;
import com.sap.andyhecd.microservice.chapter4.licenses.repository.LicenseRepository;

@Service
public class LicenseService {

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ExampleProps exampleProps;

	@Autowired
	OrganizationFeignClient organizationFeignClient;

	@Autowired
	OrganizationRestTemplateClient organizationRestClient;

	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;

	private Organization retrieveOrgInfo(String organizationId, String clientType) {
		Organization organization = null;

		switch (clientType) {
		case "feign":
			System.out.println("I am using the feign client");
			organization = organizationFeignClient.getOrganization(organizationId);
			break;
		case "rest":
			System.out.println("I am using the rest client");
			organization = organizationRestClient.getOrganization(organizationId);
			break;
		case "discovery":
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestClient.getOrganization(organizationId);
		}

		return organization;
	}

	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

		Organization org = retrieveOrgInfo(organizationId, clientType);

		return license.withOrganizationName(org.getName()).withContactName(org.getContactName())
				.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
				.withComment(exampleProps.getProperty());
	}

	public List<License> getLicensesByOrg(String organizationId) {
		List<License> findByOrganizationId = licenseRepository.findByOrganizationId(organizationId);
		findByOrganizationId.stream().forEach(license -> {
			Organization org = retrieveOrgInfo(license.getOrganizationId(), "feign");
			license.withOrganizationName(org.getName()).withContactName(org.getContactName())
					.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
					.withComment(exampleProps.getProperty());
		});

		return findByOrganizationId;
	}

	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());

		licenseRepository.save(license);

	}

	public void updateLicense(License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(License license) {
		licenseRepository.delete(license);
	}

}
