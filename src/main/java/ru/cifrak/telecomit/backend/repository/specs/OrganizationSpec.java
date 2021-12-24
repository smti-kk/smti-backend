package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;

import java.util.List;

public class OrganizationSpec {
    public static final Specification<Organization> TRUE_SPEC = (root, cq, cb) -> cb.conjunction();
    public static final Specification<Organization> FALSE_SPEC = (root, cq, cb) -> cb.disjunction();

    public static Specification<Organization> forOrCondition(Specification<Organization> spec) {
        return spec != null ? spec : OrganizationSpec.FALSE_SPEC;
    }

    public static Specification<Organization> forAndCondition(Specification<Organization> spec) {
        return spec != null ? spec : OrganizationSpec.TRUE_SPEC;
    }

    public static Specification<Organization> inLocation(Location... locations) {
        return (root, cq, cb) -> root.get(Organization_.location).in(locations);
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
        return (root, cq, cb) -> root.get(Organization_.location).get(Location_.parent).in(parents);
    }

    public static Specification<Organization> pStart(Integer pStart) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get(Organization_.location).get(Location_.population), (pStart));
    }

    public static Specification<Organization> pEnd(Integer pEnd) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get(Organization_.location).get(Location_.population), (pEnd));
    }
}
