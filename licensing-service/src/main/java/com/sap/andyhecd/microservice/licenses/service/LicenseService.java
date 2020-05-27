package com.sap.andyhecd.microservice.licenses.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sap.andyhecd.microservice.licenses.client.OrganizationDiscoveryClient;
import com.sap.andyhecd.microservice.licenses.client.OrganizationFeignClient;
import com.sap.andyhecd.microservice.licenses.client.OrganizationRestTemplateClient;
import com.sap.andyhecd.microservice.licenses.config.ExampleProps;
import com.sap.andyhecd.microservice.licenses.model.License;
import com.sap.andyhecd.microservice.licenses.model.Organization;
import com.sap.andyhecd.microservice.licenses.repository.LicenseRepository;
import com.sap.andyhecd.microservice.licenses.util.UserContextHolder;

@Service
public class LicenseService {

	private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

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

	private Organization retrieveOrgInfo(final String organizationId, final String clientType) {
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

	public License getLicense(final String organizationId, final String licenseId, final String clientType) {
		final License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

		final Organization org = retrieveOrgInfo(organizationId, clientType);

		return license.withOrganizationName(org.getName()).withContactName(org.getContactName())
				.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
				.withComment(exampleProps.getProperty());
	}

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", threadPoolKey = "licenseByOrgThreadPool", threadPoolProperties = {
			// define the maximum number of threads in the thread pool, default to 10
			@HystrixProperty(name = "coreSize", value = "30"),
			// define a queue that sits in front of your thread
			// pool and that can queue incoming requests, default to -1(no queue is used and
			// instead Hystrix will block until a thread becomes available for processing.)
			@HystrixProperty(name = "maxQueueSize", value = "10") }, commandProperties = {
					// set the length of the timeout (in milliseconds) of the circuit breaker,
					// default is 1000
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					// amount of consecutive calls that must occur within a 10-second window before
					// Hystrix will consider to trip the circuit breaker for the call. Default to 20
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					// the percentage of calls that must fail(due to timeouts, an exception being
					// thrown, or a HTTP 500 being returned) after the
					// circuitBreaker.requestVolumeThreshold value has been passed before the
					// circuit breaker it tripped, Default to 50
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
					// the amount of time Hystrix will sleep once the circuit breaker is tripped
					// before Hystrix will allow another call through to see if the service is
					// healthy again. Default to 5000
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
					// control the size of the window that will be used by Hystrix to monitor for
					// problems with a service call, default to 10000, a.k.a 10-second window,
					// Default to 10000
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
					// control the number of times statistics are collected in the window you’ve
					// defined, Default to 10
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5") })
	public List<License> getLicensesByOrgWithSleep(final String organizationId, final int sleppInMilliseconds) {
		sleep(sleppInMilliseconds);
		return getLicensesByOrg(organizationId);
	}

	public List<License> getLicensesByOrg(final String organizationId) {
		logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}",
				UserContextHolder.getContext().getCorrelationId());
		final List<License> findByOrganizationId = licenseRepository.findByOrganizationId(organizationId);
		final Organization org = retrieveOrgInfo(organizationId, "feign");
		if (Objects.nonNull(org)) {
			findByOrganizationId.stream().forEach(license -> {
				license.withOrganizationName(org.getName()).withContactName(org.getContactName())
						.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
						.withComment(exampleProps.getProperty());
			});
		}
		return findByOrganizationId;
	}

	private void sleep(final int sleppInMilliseconds) {
		try {
			Thread.sleep(sleppInMilliseconds);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private List<License> buildFallbackLicenseList(final String organizationId, final int sleppInMilliseconds) {
		final List<License> fallbackList = new ArrayList<>();
		final License license = new License().withId("0000000-00-00000").withOrganizationId(organizationId)
				.withProductName("Sorry no licensing information currently available");

		fallbackList.add(license);
		return fallbackList;
	}

	public void saveLicense(final License license) {
		license.withId(UUID.randomUUID().toString());
		licenseRepository.save(license);
	}

	public void updateLicense(final License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(final License license) {
		licenseRepository.delete(license);
	}

}
