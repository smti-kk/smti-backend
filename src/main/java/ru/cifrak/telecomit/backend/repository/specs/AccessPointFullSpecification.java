package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP_;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint_;

import java.time.LocalDate;
import java.util.List;

public class AccessPointFullSpecification {
    public static final Specification<AccessPointFull> TRUE_SPEC = (root, cq, cb) -> cb.conjunction();
    public static final Specification<AccessPointFull> FALSE_SPEC = (root, cq, cb) -> cb.disjunction();

    public static Specification<AccessPointFull> forOrCondition(Specification<AccessPointFull> spec) {
        return spec != null ? spec : AccessPointFullSpecification.FALSE_SPEC;
    }

    public static Specification<AccessPointFull> forAndCondition(Specification<AccessPointFull> spec) {
        return spec != null ? spec : AccessPointFullSpecification.TRUE_SPEC;
    }

    public static Specification<AccessPointFull> inLocations(Location... locations) {
        return (root, cq, cb) -> root.get(AccessPointFull_.organization).get(Organization_.location).in(locations);
    }

    public static Specification<AccessPointFull> inAddress(String address) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.address), address);
    }

    public static Specification<AccessPointFull> withType(TypeOrganization type) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.organization).get(Organization_.type), type);
    }

    public static Specification<AccessPointFull> withSmo(TypeSmo smo) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.organization).get(Organization_.smo), smo);
    }

    public static Specification<AccessPointFull> withGovProgram(GovernmentDevelopmentProgram gov) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.governmentDevelopmentProgram), gov);
    }

    public static Specification<AccessPointFull> withInetType(TypeInternetAccess inettype) {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.internetAccess), inettype);
    }

    public static Specification<AccessPointFull> inParent(List<Location> parents) {
        return (root, cq, cb) ->
                root.get(AccessPointFull_.organization).get(Organization_.location).get(Location_.parent).in(parents);
    }

    public static Specification<AccessPointFull> inState(List<APConnectionState> state) {
        return (root, cq, cb) ->
                root.get(AccessPointFull_.monitoringLink)
                        .get(JournalMAP_.map).get(MonitoringAccessPoint_.connectionState).in(state);
    }

    public static Specification<AccessPointFull> withOrgname(String orgname) {
        return (root, cq, cb) -> cb.like(
                cb.lower(root.get(AccessPointFull_.organization).get(Organization_.name)),
                "%" + orgname.toLowerCase() + "%");
    }

    public static Specification<AccessPointFull> withOperator(String operator) {
        return (root, cq, cb) -> cb.like(
                cb.lower(root.get(AccessPointFull_.contractor)),
                "%" + operator.toLowerCase() + "%");
    }

    public static Specification<AccessPointFull> pStart(Integer pStart) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(
                root.get(AccessPointFull_.organization).get(Organization_.location).get(Location_.population),
                pStart);
    }

    public static Specification<AccessPointFull> pEnd(Integer pEnd) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(
                root.get(AccessPointFull_.organization).get(Organization_.location).get(Location_.population),
                pEnd);
    }

    public static Specification<AccessPointFull> contract(String contract) {
        return (root, cq, cb) -> cb.like(
                cb.lower(root.get(AccessPointFull_.number)),
                "%" + contract.toLowerCase() + "%");
    }

    public static Specification<AccessPointFull> cStart(LocalDate cStart) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(
                root.get(AccessPointFull_.started),
                cStart);
    }

    public static Specification<AccessPointFull> cEnd(LocalDate cEnd) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(
                root.get(AccessPointFull_.ended),
                cEnd);
    }

    public static Specification<AccessPointFull> type(List<TypeAccessPoint> aps) {
        return (root, cq, cb) -> root.get(AccessPointFull_.type).in(aps);
    }

    public static Specification<AccessPointFull> apcontract() {
        return (root, cq, cb) -> cb.equal(root.get(AccessPointFull_.type), TypeAccessPoint.CONTRACT);
    }
}
