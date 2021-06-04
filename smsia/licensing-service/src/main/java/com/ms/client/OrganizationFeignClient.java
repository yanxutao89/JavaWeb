package com.ms.client;


import com.ms.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/v1/organizations/osc/retrieve",
            consumes = "application/json")
    Organization getOrganization(@RequestParam("organizationId") String organizationId);
}
