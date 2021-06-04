package com.ms.repository;

import com.ms.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {

}
