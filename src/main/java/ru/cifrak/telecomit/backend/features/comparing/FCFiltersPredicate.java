package ru.cifrak.telecomit.backend.features.comparing;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import org.jetbrains.annotations.Nullable;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.TcType;

import java.util.Arrays;
import java.util.List;

public class FCFiltersPredicate {
    /**
     * Added .isTrue() and .isFalse(), because .asBoolean() returns ConstantImpl, that are not casts to Predicate.
     */
    private static final BooleanExpression TRUE_EXPRESSION = Expressions.asBoolean(true).isTrue();
    private static final BooleanExpression FALSE_EXPRESSION = Expressions.asBoolean(true).isFalse();
    private final List<Integer> parentIds;
    private final String[] locationNames;
    private final List<Integer> operators;
    private final List<Integer> connectionTypes;
    private final Integer govProgram;
    private final Integer govProgramYear;
    private final Integer hasAny;
    private final TcType type;
    private final LogicalCondition logicalCondition;
    private final boolean locationNamesNotNull;

    public FCFiltersPredicate(
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAny,
            TcType type,
            LogicalCondition logicalCondition,
            String... locationNames
    ) {
        this.parentIds = parentIds;
        this.type = type;
        this.logicalCondition = logicalCondition;
        this.locationNamesNotNull = locationNamesNotNullCheck(locationNames);
        this.locationNames = locationNames != null && this.locationNamesNotNull
                ? Arrays.stream(locationNames).map(String::toLowerCase).toArray(String[]::new)
                : locationNames;
        this.operators = operators;
        this.connectionTypes = connectionTypes;
        this.hasAny = hasAny;
        this.govProgram = govProgram;
        this.govProgramYear = govProgramYear;
    }

    public BooleanExpression booleanExpression() {
        QLocationFC locationFC = QLocationFC.locationFC;
        QLocationFeature locationFeature = QLocationFeature.locationFeature;
        BooleanExpression typePredicate = getTypePredicate(locationFC);
        BooleanExpression parentPredicate = getParentPredicate(locationFC);
        BooleanExpression locationPredicate = getLocationPredicate(locationFC);
        BooleanExpression operatorPredicate = getOperatorPredicate(locationFC, locationFeature);
        BooleanExpression connectionTypePredicate = getConnectionTypePredicate(locationFC);
        BooleanExpression govProgramPredicate = getGovProgramPredicate(locationFC);
        BooleanExpression govProgramYearPredicate = getGovProgramYearPredicate(locationFC);
        BooleanExpression predicate = typePredicate;
        if (logicalCondition == LogicalCondition.OR) {
            predicate = predicate.and(
                    (parentPredicate != null ? parentPredicate : FALSE_EXPRESSION)
                            .or(locationPredicate != null ? locationPredicate : FALSE_EXPRESSION)
                            .or(operatorPredicate != null ? operatorPredicate : FALSE_EXPRESSION)
                            .or(connectionTypePredicate != null ? connectionTypePredicate : FALSE_EXPRESSION)
                            .or(govProgramPredicate != null ? govProgramPredicate : FALSE_EXPRESSION)
                            .or(govProgramYearPredicate != null ? govProgramYearPredicate : FALSE_EXPRESSION));
        } else {
            predicate = predicate.and(
                    (parentPredicate != null ? parentPredicate : TRUE_EXPRESSION)
                            .and(locationPredicate != null ? locationPredicate : TRUE_EXPRESSION)
                            .and(operatorPredicate != null ? operatorPredicate : TRUE_EXPRESSION)
                            .and(connectionTypePredicate != null ? connectionTypePredicate : TRUE_EXPRESSION)
                            .and(govProgramPredicate != null ? govProgramPredicate : TRUE_EXPRESSION)
                            .and(govProgramYearPredicate != null ? govProgramYearPredicate : TRUE_EXPRESSION));
        }
        return predicate;
    }

    private BooleanExpression getTypePredicate(QLocationFC locationFC) {
        return locationFC.type.notIn(Arrays.asList("р-н", "край", "с/с", "тер", "округ"));
    }

    @Nullable
    private BooleanExpression getGovProgramYearPredicate(QLocationFC locationFC) {
        return govProgramYear != null ?
                locationFC.technicalCapabilities.any().govYearComplete.eq(govProgramYear)
                : null;
    }

    @Nullable
    private BooleanExpression getGovProgramPredicate(QLocationFC locationFC) {
        return govProgram != null ?
                locationFC.technicalCapabilities.any().governmentDevelopmentProgram.id.eq(govProgram)
                : null;
    }

    @Nullable
    private BooleanExpression getParentPredicate(QLocationFC locationFC) {
        return parentIds != null ?
                locationFC.locationParent.id.in(parentIds)
                : null;
    }

    @Nullable
    private BooleanExpression getLocationPredicate(QLocationFC locationFC) {
        return locationNames != null && locationNames.length > 0 && locationNamesNotNull ?
                locationFC.name.toLowerCase().in(locationNames)
                : null;
    }

    @Nullable
    private BooleanExpression getConnectionTypePredicate(QLocationFC locationFC) {
        BooleanExpression result;
        if (hasAny != null) {
            result = locationFC.technicalCapabilities.any().type.eq(type);
        } else {
            if (type == TcType.INET) {
                result = connectionTypes != null ?
                        locationFC.technicalCapabilities.any().trunkChannel.id.in(connectionTypes)
                        : null;
            } else if (type == TcType.MOBILE) {
                result = connectionTypes != null ?
                        locationFC.technicalCapabilities.any().typeMobile.id.in(connectionTypes)
                        : null;
            } else {
                result = null;
            }
        }
        return result;
    }

    @Nullable
    private BooleanExpression getOperatorPredicate(QLocationFC locationFC, QLocationFeature locationFeature) {
        return operators != null ?
                JPAExpressions.selectOne().from(locationFeature).where(locationFeature.type.eq(type)
                        .and(locationFeature.operator.id.in(operators))
                        .and(locationFeature.state.ne(TcState.WAIT_FOR_STATE_TO_BE_SET))
                        .and(locationFeature.locationId.eq(locationFC.id))).exists()
                : null;
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
