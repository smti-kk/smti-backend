package ru.cifrak.telecomit.backend.api.service;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
public class TechnicalCapabilitiesServiceImpl implements TechnicalCapabilitiesService {
    private RepositoryLocation repositoryLocation;

//    private final SessionFactory sessionFactory;

    public TechnicalCapabilitiesServiceImpl(
            RepositoryLocation repositoryLocation
//            SessionFactory sessionFactory
    ) {
        this.repositoryLocation = repositoryLocation;
//        this.sessionFactory = sessionFactory;
    }

    public PaginatedList<TechnicalCapabilitiesDTO> findAll(Integer page, Integer pageSize) {
//        Session currentSession = sessionFactory.getCurrentSession();
//
//        currentSession.enableFilter("-desc");

        Page<CatalogsLocation> locationsPage = repositoryLocation.findAll(PageRequest.of(page - 1, pageSize));

        List<TechnicalCapabilitiesDTO> capabilitiesDTOS = locationsPage.getContent().stream()
                .map(TechnicalCapabilitiesDTO::new)
                .collect(Collectors.toList());

//        currentSession.disableFilter("-desc");

        return new PaginatedList<>(locationsPage.getTotalElements(), capabilitiesDTOS);
    }

    public TechnicalCapabilitiesDTO getByLocationId(Integer locationId) {
        return new TechnicalCapabilitiesDTO(repositoryLocation.getOne(locationId));
    }
}
