package com.sap.andyhecd.microservice.chapter4.organization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sap.andyhecd.microservice.chapter4.organization.model.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,String>  {
}
