package com.ms.service;

import com.ms.model.Organization;
import com.ms.repository.OrganizationRepository;
import com.ms.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization getOrganization(String organizationId) {
        System.err.println(String.format("geOrganization Correlation id: %s", UserContextHolder.getContext().getCorrelationId()));
        return organizationRepository.findById(organizationId).get();
    }

    public void saveOrganization(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organizationRepository.save(organization);
    }

    public void updateOrg(Organization organization) {
        organizationRepository.save(organization);
    }

    public void deleteOrg(Organization organization) {
        organizationRepository.delete(organization);
    }
}
