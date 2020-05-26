package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;

import java.time.LocalDate;
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

    public static Specification<AccessPoint> withOrgname(String orgname) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(AccessPoint_.organization).get(Organization_.name)), "%" + orgname.toLowerCase() + "%");
    }

    public static Specification<AccessPoint> withOperator(String operator) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(AccessPoint_.contractor)), "%" + operator.toLowerCase() + "%");
    }

    public static Specification<AccessPoint> pStart(Integer pStart) {
        return (root, cq, cb) -> cb.and(cb.greaterThanOrEqualTo(root.get(AccessPoint_.organization).get(Organization_.location).get(Location_.population), (pStart)));
    }

    public static Specification<AccessPoint> pEnd(Integer pEnd) {
        return (root, cq, cb) -> cb.and(cb.lessThanOrEqualTo(root.get(AccessPoint_.organization).get(Organization_.location).get(Location_.population), (pEnd)));
    }

   /* public static Specification<AccessPoint> ap(List<String> aps) {
        return (root, cq, cb) -> cb.and(root.get("type").in(aps));
    }*/

    public static Specification<AccessPoint> contract(String contract) {
        return (root, cq, cb) -> cb.like(cb.lower(cb.treat(root, ApContract.class).get(ApContract_.number)), "%" + contract.toLowerCase() + "%");
    }

    public static Specification<AccessPoint> cStart(LocalDate cStart) {
        return (root, cq, cb) -> cb.and(cb.greaterThanOrEqualTo(cb.treat(root, ApContract.class).get(ApContract_.started), cStart));
    }

    public static Specification<AccessPoint> cEnd(LocalDate cEnd) {
        return (root, cq, cb) -> cb.and(cb.lessThanOrEqualTo(cb.treat(root, ApContract.class).get(ApContract_.started), cEnd));
    }
}
