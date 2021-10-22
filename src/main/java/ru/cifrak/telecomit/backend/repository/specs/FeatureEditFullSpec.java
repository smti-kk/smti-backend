package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeature_;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class FeatureEditFullSpec {
    public static Specification<FeatureEditFull> inUser(List<User> users) {
        return (root, cq, cb) -> root.get(FeatureEditFull_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.user)
                .in(users);
    }

    public static Specification<FeatureEditFull> inLocation(List<LocationForTable> locations) {
        return (root, cq, cb) -> root.get(FeatureEditFull_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.location)
                .in(locations);
    }

    public static Specification<FeatureEditFull> inParent(List<LocationParent> parents) {
        return (root, cq, cb) -> root.get(FeatureEditFull_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.location)
                .get(LocationForTable_.locationParent)
                .in(parents);
    }

    public static Specification<FeatureEditFull> greaterThan(LocalDate contractStart) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(
                root.get(FeatureEditFull_.locationFeaturesEditingRequest)
                        .get(LocationFeaturesEditingRequestFull1_.created),
                LocalDateTime.of(contractStart, LocalTime.MIN)
        );
    }

    public static Specification<FeatureEditFull> lessThan(LocalDate contractEnd) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(
                root.get(FeatureEditFull_.locationFeaturesEditingRequest)
                        .get(LocationFeaturesEditingRequestFull1_.created),
                LocalDateTime.of(contractEnd, LocalTime.MAX)
        );
    }

    public static Specification<FeatureEditFull> inAction(List<FeatureEditAction> actions) {
        return (root, cq, cb) -> root.get(FeatureEditFull_.action).in(actions);
    }

    public static Specification<FeatureEditFull> excludeDuplicateChanges() {
        return (root, cq, cb) ->
                cb.or(
                        cb.notEqual(root.get(FeatureEditFull_.action), FeatureEditAction.UPDATE),
                        cb.and(
                                cb.equal(root.get(FeatureEditFull_.action), FeatureEditAction.UPDATE),
                                cb.or(
                                        cb.and(
                                                cb.equal(root.get(FeatureEditFull_.tc).get(LocationFeature_.type), TcType.INET),
                                                cb.or(
                                                        cb.equal(
                                                                root.get(FeatureEditFull_.tc).get(LocationFeature_.trunkChannel),
                                                                root.get(FeatureEditFull_.newValue).get(LocationFeature_.trunkChannel)
                                                        ),
                                                        cb.notEqual(
                                                                root.get(FeatureEditFull_.tc).get(LocationFeature_.quality),
                                                                root.get(FeatureEditFull_.newValue).get(LocationFeature_.quality)
                                                        )
                                                )
                                        ),
                                        cb.and(
                                                cb.notEqual(root.get(FeatureEditFull_.tc).get(LocationFeature_.type), TcType.INET)
                                        )
                                )
                        )
                );
    }
}
