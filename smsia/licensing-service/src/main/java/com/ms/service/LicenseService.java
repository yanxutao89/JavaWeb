package com.ms.service;

import com.ms.client.OrganizationDiscoveryClient;
import com.ms.client.OrganizationFeignClient;
import com.ms.client.OrganizationRestTemplateClient;
import com.ms.config.ServiceConfig;
import com.ms.model.License;
import com.ms.model.Organization;
import com.ms.repository.LicenseRepository;
import com.ms.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
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

    @HystrixCommand(
//            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrganizationThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public List<License> getLicensesByOrganization(String organizationId) {
        System.err.println(String.format("getLicensesByOrganization Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));
//        randomlyRunLong();
        Organization organization = organizationFeignClient.getOrganization(organizationId);
        System.out.println(organization);
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public License getLicense(String organizationId, String licenseId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrganizationInfo(organizationId, clientType);

        return license
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone())
                .withComment(config.getExampleProperty());
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

    public List<License> buildFallbackLicenseList(String organizationId) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("000000-00-000000")
                .withOrganizationId(organizationId)
                .withProductName(
                "Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }

    private void randomlyRunLong() {
        Random random = new Random();
        int randomNum = random.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
