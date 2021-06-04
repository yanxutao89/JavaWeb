package com.ms.controller;

import com.ms.config.ServiceConfig;
import com.ms.model.License;
import com.ms.service.LicenseService;
import com.ms.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping(value = "/lsc")
public class LicenseServiceController {
    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;

    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    public List<License> getLicenses(@RequestParam("organizationId") String organizationId) {
        System.err.println(String.format("getLicenses Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));
        return licenseService.getLicensesByOrganization(organizationId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateLicenses(@RequestParam("licenseId") String licenseId, @RequestBody License license) {
        licenseService.updateLicense(license);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicenses(@RequestParam("licenseId") String licenseId, @RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
