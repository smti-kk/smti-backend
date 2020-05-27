package ru.cifrak.telecomit.backend.api.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.Location;

public class LocationSpecification {
    public static Specification<Location> isPublished(boolean isPublished) {
        return (vote, cq, cb) -> cb.equal(vote.get("isPublished"), isPublished);
    }

    public static Specification<Location> hasLocality(Long locality) {
        return (vote, cq, cb) -> cb.isMember(locality, vote.get("locality"));
    }

    public static Specification<Location> isOpened(boolean isOpened) {
        return (vote, cq, cb) -> cb.equal(vote.get("isOpened"), isOpened);
    }
}
