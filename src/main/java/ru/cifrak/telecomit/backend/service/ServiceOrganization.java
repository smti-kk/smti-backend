package ru.cifrak.telecomit.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.util.List;

@Transactional
@Service
public class ServiceOrganization {
    private final RepositoryOrganization rOrganization;

    public ServiceOrganization(RepositoryOrganization rOrganization) {
        this.rOrganization = rOrganization;
    }


    public List<Organization> all() {
        return rOrganization.findAll();
    }

    public OrganizationDTO getOrganizationById(Integer id) {
            return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }


}
