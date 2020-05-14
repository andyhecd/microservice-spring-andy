package com.sap.andyhecd.microservice.organization.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.andyhecd.microservice.organization.model.Organization;
import com.sap.andyhecd.microservice.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
	@Autowired
	private OrganizationRepository orgRepository;

	public Organization getOrg(String organizationId) {
		Optional<Organization> findById = orgRepository.findById(organizationId);
		return findById.isPresent() ? findById.get() : null;
	}

	public void saveOrg(Organization org) {
		org.setId(UUID.randomUUID().toString());

		orgRepository.save(org);

	}

	public void updateOrg(Organization org) {
		orgRepository.save(org);
	}

	public void deleteOrg(Organization org) {
		orgRepository.delete(org);
	}
}