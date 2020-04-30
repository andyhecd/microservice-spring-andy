package com.sap.andyhecd.microservice.chapter2.licenses.service;

import org.springframework.stereotype.Service;

import com.sap.andyhecd.microservice.chapter2.licenses.model.License;

@Service
public class LicenseService {

    public License getLicense(String licenseId, String organizationId){
        return new License()
                .withId(licenseId)
                .withOrganizationId( organizationId )
                .withProductName("Test Product Name")
                .withLicenseType("PerSeat");
    }

    public void saveLicense(License license){

    }

    public void updateLicense(License license){

    }

    public void deleteLicense(License license){

    }

}
