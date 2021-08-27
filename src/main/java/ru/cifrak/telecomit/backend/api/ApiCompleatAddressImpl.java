package ru.cifrak.telecomit.backend.api;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.AccessPointFull_;
import ru.cifrak.telecomit.backend.entities.Organization_;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.util.List;
@RestController()
@RequestMapping("/api/autocomplit")
public class ApiCompleatAddressImpl implements ApiCompleatAddress {

    private final RepositoryOrganization rOrganization;

    public ApiCompleatAddressImpl(RepositoryOrganization rOrganization) {
        this.rOrganization = rOrganization;
    }

    @Override
    @GetMapping()
    public List<AddressDto> getVariantsForCompleat(String address) {
        return rOrganization.findAll(PointSpecification.search(address));
    }

    public static Specification<AccessPointFull> inAddress(String address) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.organization).get(Organization_.address), address);
    }
}
