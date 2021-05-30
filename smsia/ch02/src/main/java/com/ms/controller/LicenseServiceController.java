package com.ms.controller;

import com.ms.model.License;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{organizationId}/licenses")
public class LicenseServiceController {

    @RequestMapping(value="/{licenseId}", method = RequestMethod.GET)
    public License getLicenses(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        return new License()
                .withId(licenseId)
                .withProductName("Teleco")
                .withLicenseType("Seat")
                .withOrganizationId("TestOrg");
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.PUT)
    public String updateLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the put");
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.POST)
    public String saveLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the post");
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the Delete");
    }

}
