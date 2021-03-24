package ru.cifrak.telecomit.backend.features.comparing;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import ru.cifrak.telecomit.backend.entities.TcType;

import java.util.Arrays;
import java.util.List;

public class FCFiltersPredicate {

    private final List<Integer> parentIds;
    private final String[] locationNames;
    private final List<Integer> internetOperators;
    private final List<Integer> mobileOperators;
    private final List<Integer> connectionTypes;
    private final List<Integer> mobileTypes;
    private final Integer govProgram;
    private final Integer govProgramYear;
    private final Integer hasAnyInternet;
    private final Integer hasAnyMobile;
    private boolean locationNamesNotNull;

    public FCFiltersPredicate(
            List<Integer> parentIds,
            List<Integer> internetOperators,
            List<Integer> mobileOperators,
            List<Integer> connectionTypes,
            List<Integer> mobileTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAnyInternet,
            Integer hasAnyMobile,
            String... locationNames
    ) {
        this.parentIds = parentIds;
        this.locationNamesNotNull = locationNamesNotNullCheck(locationNames);
        this.locationNames = locationNames != null && this.locationNamesNotNull
                ? Arrays.stream(locationNames).map(String::toLowerCase).toArray(String[]::new)
                : locationNames;
        this.internetOperators = internetOperators;
        this.mobileOperators = mobileOperators;
        this.connectionTypes = connectionTypes;
        this.mobileTypes = mobileTypes;
        this.govProgram = govProgram;
        this.govProgramYear = govProgramYear;
        this.hasAnyInternet = hasAnyInternet;
        this.hasAnyMobile = hasAnyMobile;
    }

    public BooleanExpression booleanExpression() {
        QLocationFC locationFC = QLocationFC.locationFC;
        QLocationFeature locationFeature = QLocationFeature.locationFeature;
        com.querydsl.core.types.dsl.BooleanExpression predicate = locationFC.type.notIn(Arrays.asList("р-н", "край", "с/с", "тер", "округ"));
        if (parentIds != null) {
            predicate = predicate.and(locationFC.locationParent.id.in(parentIds));
        }
        if (locationNames != null && locationNames.length > 0) {
            if (locationNamesNotNull) {
                predicate = predicate.and(locationFC.name.toLowerCase().in(locationNames));
            }
        }
        if (internetOperators != null) {
            predicate = predicate.and(
                    JPAExpressions
                            .selectOne()
                            .from(locationFeature)
                            .where(
                                    locationFeature.type.eq(TcType.INET)
                                            .and(locationFeature.operator.id.in(internetOperators))
                                            .and(locationFC.id.eq(locationFeature.locationId))
                            )
                            .exists()
            );
        }
        if (mobileOperators != null) {
            predicate = predicate.and(
                    JPAExpressions
                            .selectOne()
                            .from(locationFeature)
                            .where(
                                    locationFeature.type.eq(TcType.MOBILE)
                                            .and(locationFeature.operator.id.in(mobileOperators))
                                            .and(locationFC.id.eq(locationFeature.locationId))
                            )
                            .exists()
            );
        }
        if (connectionTypes != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().trunkChannel.id.in(connectionTypes));
        }
        if (mobileTypes != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().typeMobile.id.in(mobileTypes));
        }
        if (govProgram != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().governmentDevelopmentProgram.id.eq(govProgram));
        }
        if (govProgramYear != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().govYearComplete.eq(govProgramYear));
        }
        if (hasAnyInternet != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().type.eq(TcType.INET));
        }
        if (hasAnyMobile != null) {
            predicate = predicate.and(locationFC.technicalCapabilities.any().type.eq(TcType.MOBILE));
        }
        return predicate;
    }

    private boolean locationNamesNotNullCheck(String... locationNames) {
        boolean result = true;
        if (locationNames != null && locationNames.length > 0) {
            for (String name : locationNames) {
                if (name == null) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
