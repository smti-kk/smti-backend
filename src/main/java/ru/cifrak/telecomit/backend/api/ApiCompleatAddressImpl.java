package ru.cifrak.telecomit.backend.api;

import liquibase.pro.packaged.A;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.AccessPointFull_;
import ru.cifrak.telecomit.backend.entities.Organization_;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/autocomplit")
public class ApiCompleatAddressImpl implements ApiCompleatAddress {

    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints accessPoints;

    public ApiCompleatAddressImpl(RepositoryOrganization rOrganization, RepositoryAccessPoints accessPoints) {
        this.rOrganization = rOrganization;
        this.accessPoints = accessPoints;
    }

    @Override
    @GetMapping()
    public List<AddressDto> getVariantsForCompleat(String address) {
        List<AccessPoint> pageDatas =  accessPoints.findAll(PointSpecification.search(address));
        return pageDatas.stream().map((accessPoint) -> new AddressDto(1,accessPoint.getAddress())).collect(Collectors.toList());
    }

    public static Specification<AccessPointFull> inAddress(String address) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.organization).get(Organization_.address), address);
    }
}
