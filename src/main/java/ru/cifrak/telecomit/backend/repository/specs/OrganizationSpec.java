package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.Organization_;

import java.util.List;

public class OrganizationSpec {
    public static Specification<Organization> inLocation(Location location) {
        return (root, cq, cb) -> cb.equal(root.get(Organization_.location), location);
    }

    public static Specification<Organization> withType(TypeOrganization type) {
        return (root, cq, cb) -> cb.equal(root.get(Organization_.type), type);
    }

    public static Specification<Organization> withSmo(TypeSmo smo) {
        return (root, cq, cb) -> cb.equal(root.get(Organization_.smo), smo);
    }

    public static Specification<Organization> withOrgname(String orgname) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Organization_.name)), "%" + orgname.toLowerCase() + "%");
    }

    public static Specification<Organization> inParent(List<Location> parents) {
        return (root, cq, cb) -> cb.and(root.get(Organization_.location).get(Location_.parent).in(parents));
    }

    public static Specification<Organization> pStart(Integer pStart) {
        return (root, cq, cb) -> cb.and(cb.greaterThanOrEqualTo(root.get(Organization_.location).get(Location_.population), (pStart)));
    }

    public static Specification<Organization> pEnd(Integer pEnd) {
        return (root, cq, cb) -> cb.and(cb.lessThanOrEqualTo(root.get(Organization_.location).get(Location_.population), (pEnd)));
    }
}
