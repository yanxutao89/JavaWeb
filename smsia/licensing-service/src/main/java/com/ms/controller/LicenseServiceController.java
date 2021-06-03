package com.ms.controller;

import com.ms.config.ServiceConfig;
import com.ms.model.License;
import com.ms.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping(value = "/{organizationId}/licenses")
public class LicenseServiceController {
    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<License> getLicenses(@RequestParam("organizationId") String organizationId) {
        return licenseService.getLicensesByOrg(organizationId);
    }

    @RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
    public License getLicenses(@RequestParam("organizationId") String organizationId,
                               @RequestParam("licenseId") String licenseId) {
        return licenseService.getLicense(organizationId, licenseId, "");
    }

    @RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
    public License getLicensesWithClient(@RequestParam("organizationId") String organizationId,
                                         @RequestParam("licenseId") String licenseId,
                                         @RequestParam("clientType") String clientType) {
        return licenseService.getLicense(organizationId, licenseId, clientType);
    }

    @RequestMapping(value = "/{licenseId}", method = RequestMethod.PUT)
    public void updateLicenses(@RequestParam("licenseId") String licenseId, @RequestBody License license) {
        licenseService.updateLicense(license);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @RequestMapping(value = "/{licenseId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicenses(@RequestParam("licenseId") String licenseId, @RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
