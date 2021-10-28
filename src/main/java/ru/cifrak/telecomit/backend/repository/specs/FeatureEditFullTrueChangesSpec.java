package ru.cifrak.telecomit.backend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class FeatureEditFullTrueChangesSpec {
    public static final Specification<FeatureEditFullTrueChanges> TRUE_SPEC = (root, cq, cb) -> cb.conjunction();
    public static final Specification<FeatureEditFullTrueChanges> FALSE_SPEC = (root, cq, cb) -> cb.disjunction();

    public static Specification<FeatureEditFullTrueChanges> forOrCondition(
            Specification<FeatureEditFullTrueChanges> spec) {
        return spec != null ? spec : FeatureEditFullTrueChangesSpec.FALSE_SPEC;
    }

    public static Specification<FeatureEditFullTrueChanges> forAndCondition(
            Specification<FeatureEditFullTrueChanges> spec) {
        return spec != null ? spec : FeatureEditFullTrueChangesSpec.TRUE_SPEC;
    }

    public static Specification<FeatureEditFullTrueChanges> inStatus(List<EditingRequestStatus> statuses) {
        return (root, cq, cb) -> root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.status)
                .in(statuses);
    }

    public static Specification<FeatureEditFullTrueChanges> inUser(List<User> users) {
        return (root, cq, cb) -> root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.user)
                .in(users);
    }

    public static Specification<FeatureEditFullTrueChanges> inLocation(List<LocationForTable> locations) {
        return (root, cq, cb) -> root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.location)
                .in(locations);
    }

    public static Specification<FeatureEditFullTrueChanges> inParent(List<LocationParent> parents) {
        return (root, cq, cb) -> root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                .get(LocationFeaturesEditingRequestFull1_.location)
                .get(LocationForTable_.locationParent)
                .in(parents);
    }

    public static Specification<FeatureEditFullTrueChanges> greaterThan(LocalDate contractStart) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(
                root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                        .get(LocationFeaturesEditingRequestFull1_.created),
                LocalDateTime.of(contractStart, LocalTime.MIN)
        );
    }

    public static Specification<FeatureEditFullTrueChanges> lessThan(LocalDate contractEnd) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(
                root.get(FeatureEditFullTrueChanges_.locationFeaturesEditingRequest)
                        .get(LocationFeaturesEditingRequestFull1_.created),
                LocalDateTime.of(contractEnd, LocalTime.MAX)
        );
    }

    public static Specification<FeatureEditFullTrueChanges> inAction(List<FeatureEditAction> actions) {
        return (root, cq, cb) -> root.get(FeatureEditFullTrueChanges_.action).in(actions);
    }
}
