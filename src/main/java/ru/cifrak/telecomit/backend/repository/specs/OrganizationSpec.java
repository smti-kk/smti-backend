package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.Organization_;

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
}
