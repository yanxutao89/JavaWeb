package com.order.controller;


import com.order.model.Organization;
import com.order.service.OrganizationService;
import com.order.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(value = "/osc")
public class OrganizationServiceController {
    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    public Organization getOrganization(@RequestParam("organizationId") String organizationId) {
        System.err.println(String.format("getOrganization Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));
        return organizationService.getOrganization(organizationId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateOrganization(@RequestParam("organizationId") String organizationId, @RequestBody Organization org) {
        organizationService.updateOrg(org);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void saveOrganization(@RequestParam Organization organization) {
        organizationService.saveOrganization(organization);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@RequestParam("organizationId") String organizationId, @RequestBody Organization org) {
        organizationService.deleteOrg(org);
    }
}
