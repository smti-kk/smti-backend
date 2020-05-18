package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.AccessPoint_;
import ru.cifrak.telecomit.backend.entities.Location_;
import ru.cifrak.telecomit.backend.entities.Organization_;

import java.util.List;

public class SpecificationAccessPoint {
    public static Specification<AccessPoint> inLocation(Location location) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPoint_.organization).get(Organization_.location), location);
    }
    public static Specification<AccessPoint> withType(TypeOrganization type) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPoint_.organization).get(Organization_.type), type);
    }
    public static Specification<AccessPoint> withSmo(TypeSmo smo) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPoint_.organization).get(Organization_.smo), smo);
    }
    public static Specification<AccessPoint> withGovProgram(GovernmentDevelopmentProgram gov) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPoint_.governmentDevelopmentProgram), gov);
    }

    public static Specification<AccessPoint> withInetType(TypeInternetAccess inettype) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPoint_.internetAccess), inettype);
    }

    public static Specification<AccessPoint> inParent(List<Location> parents) {
        return (root, cq, cb) -> cb.and(root.get(AccessPoint_.organization).get(Organization_.location).get(Location_.parent).in(parents));
    }
}
