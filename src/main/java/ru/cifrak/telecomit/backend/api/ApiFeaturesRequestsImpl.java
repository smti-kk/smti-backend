package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.repository.specs.OrganizationSpec;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;
import ru.cifrak.telecomit.backend.utils.SortingOrder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {

    private static final Map<String, String[]> SORTING_FIELDS = new HashMap<String, String[]>() {{
        put("id", new String[]{"id"});
        put("created", new String[]{"locationFeaturesEditingRequest.created"});
        put("source", new String[]{"locationFeaturesEditingRequest.changeSource.toString()"});
        put("action", new String[]{"action.toString()"});
        put("user", new String[]{"locationFeaturesEditingRequest.user.oid"});
        put("userName", new String[]{
                "locationFeaturesEditingRequest.user.lastName",
                "locationFeaturesEditingRequest.user.firstName",
                "locationFeaturesEditingRequest.user.patronymicName"});
        put("location", new String[]{"locationFeaturesEditingRequest.location.name"});
        put("locationParent", new String[]{"locationFeaturesEditingRequest.location.locationParent.name"});
    }};

    private final RepositoryFeaturesRequests repositoryFeaturesRequests;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryLocation repositoryLocation;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final LocationService locationService;
    private final RepositoryFeatureEditFulls repositoryFeatureEditFulls;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests,
                                   RepositoryAccount repositoryAccount,
                                   RepositoryLocation repositoryLocation,
                                   RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                                   ServiceWritableTc serviceWritableTc,
                                   LocationService locationService,
                                   RepositoryFeatureEditFulls repositoryFeatureEditFulls) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
        this.repositoryAccount = repositoryAccount;
        this.repositoryLocation = repositoryLocation;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
        this.locationService = locationService;
        this.repositoryFeatureEditFulls = repositoryFeatureEditFulls;
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable) {
        log.info("->GET /api/features-requests/");
        log.info("<- GET /api/features-requests/");
        return repositoryFeaturesRequests.findAllRequests(pageable);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsAndImportsAndEditions(
            Pageable pageable,
            List<String> sort,
            List<Integer> locationIds,
            List<Integer> parentIds,
            LocalDate contractStart,
            LocalDate contractEnd,
            List<Integer> actionIds,
            List<Integer> userIds) {
        log.info("->GET /api/features-requests/full/");
        log.info("<- GET /api/features-requests/full/");

//        Specification<FeatureEditFull> spec = Specification.where(null);
//        if (locationIds != null && locationIds.size() > 0) {
//            spec = spec.and(OrganizationSpec.inLocation(locations));
//        }
//        if (type != null) {
//            spec = spec.and(OrganizationSpec.withType(type));
//        }
//        if (smo != null) {
//            spec = spec.and(OrganizationSpec.withSmo(smo));
//        }

//        if (gdp != null) {
//            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
//        }
//        if (inettype != null) {
//            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
//        }

//        if (parents != null) {
//            spec = spec.and(OrganizationSpec.inParent(parents));
//        }
//        if (organization != null) {
//            spec = spec.and(OrganizationSpec.withOrgname(organization));
//        }

//        if (contractor != null) {
//            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
//        }

//        if (pStart != null) {
//            spec = spec.and(OrganizationSpec.pStart(pStart));
//        }
//        if (pEnd != null) {
//            spec = spec.and(OrganizationSpec.pEnd(pEnd));
//        }

//        if (ap != null) {
//            spec = spec.and(SpecificationAccessPointFull.type(ap));
//        }
//        Page<Organization> pageDatas = rOrganization.findAll(spec, pageConfig);

        return getLocationFeaturesEditingRequestFulls(checkSort(pageable, sort));
    }

    private Pageable checkSort(Pageable pageable, List<String> sort) {
        if (sort != null && sort.size() == 2
                && SORTING_FIELDS.keySet().stream().anyMatch(field -> field.equals(sort.get(0)))
                && Arrays.stream(SortingOrder.values()).map(Enum::toString).anyMatch(order -> order.equals(sort.get(1).toUpperCase()))) {
            Sort sortingSection = Sort.by(SORTING_FIELDS.get(sort.get(0)));
            if (sort.get(1).toUpperCase().equals(SortingOrder.ASC.toString())) {
                sortingSection = sortingSection.ascending();
            } else {
                sortingSection = sortingSection.descending();
            }
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    sortingSection);
        }
        return pageable;
    }

    @NotNull
    private Page<LocationFeaturesEditingRequestFull> getLocationFeaturesEditingRequestFulls(Pageable pageable) {
        Page<FeatureEditFull> allRequestsAndImportAndEditions = repositoryFeatureEditFulls.findAllRequestsAndImportAndEditions(pageable);
        return new PageImpl<>(
                transferFeatureEditFullContent2LocationFeaturesEditingRequestFullContent(allRequestsAndImportAndEditions.getContent()),
                allRequestsAndImportAndEditions.getPageable(),
                allRequestsAndImportAndEditions.getTotalElements());
    }

    private List<LocationFeaturesEditingRequestFull> transferFeatureEditFullContent2LocationFeaturesEditingRequestFullContent(List<FeatureEditFull> content) {
        List<LocationFeaturesEditingRequestFull> result = new ArrayList<>();
        for (FeatureEditFull featureEdit : content) {
            LocationFeaturesEditingRequestFull request2LocationFeaturesEditingRequestFull = new LocationFeaturesEditingRequestFull();
            LocationFeaturesEditingRequestFull1 requestFromFeatureEditFull = featureEdit.getLocationFeaturesEditingRequest();
            request2LocationFeaturesEditingRequestFull.setId(requestFromFeatureEditFull.getId());
            request2LocationFeaturesEditingRequestFull.setLocation(requestFromFeatureEditFull.getLocation());
            request2LocationFeaturesEditingRequestFull.setComment(requestFromFeatureEditFull.getComment());
            request2LocationFeaturesEditingRequestFull.setDeclineComment(requestFromFeatureEditFull.getDeclineComment());
            request2LocationFeaturesEditingRequestFull.setCreated(requestFromFeatureEditFull.getCreated());
            request2LocationFeaturesEditingRequestFull.setUser(requestFromFeatureEditFull.getUser());
            request2LocationFeaturesEditingRequestFull.setStatus(requestFromFeatureEditFull.getStatus());
            request2LocationFeaturesEditingRequestFull.setChangeSource(requestFromFeatureEditFull.getChangeSource());
            request2LocationFeaturesEditingRequestFull.setFeatureEdits(setOfOneFeature(featureEdit));
            result.add(request2LocationFeaturesEditingRequestFull);
        }
        return result;
    }

    private Set<FeatureEditFull> setOfOneFeature(FeatureEditFull feature) {
        FeatureEditFull feature2Set = new FeatureEditFull();
        feature2Set.setId(feature.getId());
        feature2Set.setAction(feature.getAction());
        feature2Set.setTc(feature.getTc());
        feature2Set.setNewValue(feature.getNewValue());
        return Collections.singleton(feature2Set);
    }

    @Override
    public List<LocationFeaturesEditingRequestFull> requestsByLocation(Integer locationId) {
        log.info("->GET /api/features-requests/::{}", locationId);
        log.info("<- GET /api/features-requests/::{}", locationId);
        return repositoryFeaturesRequests.findAllByLocationIdOrderByCreatedDesc(locationId);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable, User user) {
        log.info("->GET /api/features-requests/by-user");
        log.info("<- GET /api/features-requests/by-user");
        List<Location> locationsByUser =
                repositoryAccount.findById(user.getId()).orElseThrow(NotFoundException::new)
                        .getLocations().stream().map(l ->
                                repositoryLocation.get(l.getId())).collect(Collectors.toList());
        return repositoryFeaturesRequests.findByLocation_IdIn(locationsByUser, pageable);
    }

    @Override
    public void acceptRequest(LocationFeaturesEditingRequest request, User user) {
        log.info("->GET /api/features-requests/{request}/accept");
        log.info("<- GET /api/features-requests/{request}/accept");
        request.accept(serviceWritableTc);
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }

    @Override
    public void declineRequest(LocationFeaturesEditingRequest request,
                               String comment,
                               User user) {
        log.info("->GET /api/features-requests/{request}/decline");
        log.info("<- GET /api/features-requests/{request}/decline");
        request.decline(comment);
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }
}
