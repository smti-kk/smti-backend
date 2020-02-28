package ru.cifrak.telecomit.backend.api.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;

public class LocationSpecification {
    public static Specification<CatalogsLocation> isPublished(boolean isPublished) {
        return (vote, cq, cb) -> cb.equal(vote.get("isPublished"), isPublished);
    }

    public static Specification<CatalogsLocation> hasLocality(Long locality) {
        return (vote, cq, cb) -> cb.isMember(locality, vote.get("locality"));
    }

    public static Specification<CatalogsLocation> isOpened(boolean isOpened) {
        return (vote, cq, cb) -> cb.equal(vote.get("isOpened"), isOpened);
    }
}
