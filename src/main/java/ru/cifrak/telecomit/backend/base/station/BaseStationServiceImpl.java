package ru.cifrak.telecomit.backend.base.station;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.base.station.entity.QBaseStation;
import ru.cifrak.telecomit.backend.base.station.repository.BaseStationsRepository;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class BaseStationServiceImpl implements BaseStationService {

    /**
     * Added .isTrue() and .isFalse(), because .asBoolean() returns ConstantImpl, that are not casts to Predicate.
     */
    private static final BooleanExpression TRUE_EXPRESSION = Expressions.asBoolean(true).isTrue();
    private static final BooleanExpression FALSE_EXPRESSION = Expressions.asBoolean(true).isFalse();
    private final BaseStationsRepository baseStationsRepository;

    public BaseStationServiceImpl(BaseStationsRepository baseStationsRepository) {
        this.baseStationsRepository = baseStationsRepository;
    }

    @Override
    public Page<BaseStation> baseStations(Pageable pageable,
                                          @Nullable List<Integer> operatorIds,
                                          @Nullable List<Integer> typeMobiles,
                                          @Nullable Double coverageRadiusLeftBorder,
                                          @Nullable Double coverageRadiusRightBorder,
                                          @Nullable Double propHeightLeftBorder,
                                          @Nullable Double propHeightRightBorder,
                                          @Nullable Date actionDateFrom,
                                          @Nullable Date actionDateTo,
                                          @Nullable String address,
                                          @Nullable LogicalCondition logicalCondition) {
        return baseStationsRepository.findAll(
                getPredicate(operatorIds,
                        typeMobiles,
                        coverageRadiusLeftBorder,
                        coverageRadiusRightBorder,
                        propHeightLeftBorder,
                        propHeightRightBorder,
                        actionDateFrom,
                        actionDateTo,
                        address,
                        logicalCondition),
                pageable);
    }

    private BooleanExpression getPredicate(@Nullable List<Integer> operatorIds, @Nullable List<Integer> typeMobiles, @Nullable Double coverageRadiusLeftBorder, @Nullable Double coverageRadiusRightBorder, @Nullable Double propHeightLeftBorder, @Nullable Double propHeightRightBorder, @Nullable Date actionDateFrom, @Nullable Date actionDateTo, @Nullable String address, @Nullable LogicalCondition logicalCondition) {
        QBaseStation qBaseStation = QBaseStation.baseStation;
        BooleanExpression operatorIdsPredicate = getOperatorIdsPredicate(operatorIds, qBaseStation);
        BooleanExpression typeMobilesPredicate = getTypeMobilesPredicate(typeMobiles, qBaseStation);
        BooleanExpression coverageRadiusLeftBorderPredicate =
                getCoverageRadiusLeftBorderPredicate(coverageRadiusLeftBorder, qBaseStation);
        BooleanExpression coverageRadiusRightBorderPredicate =
                getCoverageRadiusRightBorderPredicate(coverageRadiusRightBorder, qBaseStation);
        BooleanExpression propHeightLeftBorderPredicate =
                getPropHeightLeftBorderPredicate(propHeightLeftBorder, qBaseStation);
        BooleanExpression propHeightRightBorderPredicate =
                getPropHeightRightBorderPredicate(propHeightRightBorder, qBaseStation);
        BooleanExpression actionDateFromPredicate = getActionDateFromPredicate(actionDateFrom, qBaseStation);
        BooleanExpression actionDateToPredicate = getActionDateToPredicate(actionDateTo, qBaseStation);
        BooleanExpression addressPredicate = getAddressPredicate(address, qBaseStation);
        BooleanExpression predicate;
        if (logicalCondition == LogicalCondition.OR) {
            predicate = (operatorIdsPredicate != null ? operatorIdsPredicate : FALSE_EXPRESSION)
                    .or(typeMobilesPredicate != null ? typeMobilesPredicate : FALSE_EXPRESSION)
                    .or(coverageRadiusLeftBorderPredicate != null ? coverageRadiusLeftBorderPredicate : FALSE_EXPRESSION)
                    .or(coverageRadiusRightBorderPredicate != null ? coverageRadiusRightBorderPredicate : FALSE_EXPRESSION)
                    .or(propHeightLeftBorderPredicate != null ? propHeightLeftBorderPredicate : FALSE_EXPRESSION)
                    .or(propHeightRightBorderPredicate != null ? propHeightRightBorderPredicate : FALSE_EXPRESSION)
                    .or(actionDateFromPredicate != null ? actionDateFromPredicate : FALSE_EXPRESSION)
                    .or(actionDateToPredicate != null ? actionDateToPredicate : FALSE_EXPRESSION)
                    .or(addressPredicate != null ? addressPredicate : FALSE_EXPRESSION);
        } else {
            predicate = (operatorIdsPredicate != null ? operatorIdsPredicate : TRUE_EXPRESSION)
                    .and(typeMobilesPredicate != null ? typeMobilesPredicate : TRUE_EXPRESSION)
                    .and(coverageRadiusLeftBorderPredicate != null ? coverageRadiusLeftBorderPredicate : TRUE_EXPRESSION)
                    .and(coverageRadiusRightBorderPredicate != null ? coverageRadiusRightBorderPredicate : TRUE_EXPRESSION)
                    .and(propHeightLeftBorderPredicate != null ? propHeightLeftBorderPredicate : TRUE_EXPRESSION)
                    .and(propHeightRightBorderPredicate != null ? propHeightRightBorderPredicate : TRUE_EXPRESSION)
                    .and(actionDateFromPredicate != null ? actionDateFromPredicate : TRUE_EXPRESSION)
                    .and(actionDateToPredicate != null ? actionDateToPredicate : TRUE_EXPRESSION)
                    .and(addressPredicate != null ? addressPredicate : TRUE_EXPRESSION);
        }
        return predicate;
    }

    private BooleanExpression getTypeMobilesPredicate(@Nullable List<Integer> typeMobiles, QBaseStation qBaseStation) {
        return typeMobiles != null ?
                qBaseStation.mobileType.id.in(typeMobiles)
                : null;
    }

    private BooleanExpression getOperatorIdsPredicate(@Nullable List<Integer> operatorIds, QBaseStation qBaseStation) {
        return operatorIds != null ?
                qBaseStation.operator.id.in(operatorIds)
                : null;
    }

    private BooleanExpression getAddressPredicate(@Nullable String address, QBaseStation qBaseStation) {
        return address != null ?
                qBaseStation.address.containsIgnoreCase(address)
                : null;
    }

    private BooleanExpression getActionDateToPredicate(@Nullable Date actionDateTo, QBaseStation qBaseStation) {
        return actionDateTo != null ?
                qBaseStation.actionDate.before(actionDateTo)
                : null;
    }

    private BooleanExpression getActionDateFromPredicate(@Nullable Date actionDateFrom, QBaseStation qBaseStation) {
        return actionDateFrom != null ?
                qBaseStation.actionDate.after(actionDateFrom)
                : null;
    }

    private BooleanExpression getPropHeightRightBorderPredicate(@Nullable Double propHeightRightBorder, QBaseStation qBaseStation) {
        return propHeightRightBorder != null ?
                qBaseStation.propHeight.loe(propHeightRightBorder)
                : null;
    }

    private BooleanExpression getPropHeightLeftBorderPredicate(@Nullable Double propHeightLeftBorder, QBaseStation qBaseStation) {
        return propHeightLeftBorder != null ?
                qBaseStation.propHeight.goe(propHeightLeftBorder)
                : null;
    }

    private BooleanExpression getCoverageRadiusRightBorderPredicate(@Nullable Double coverageRadiusRightBorder, QBaseStation qBaseStation) {
        return coverageRadiusRightBorder != null ?
                qBaseStation.coverageRadius.loe(coverageRadiusRightBorder)
                : null;
    }

    @Nullable
    private BooleanExpression getCoverageRadiusLeftBorderPredicate(@Nullable Double coverageRadiusLeftBorder, QBaseStation qBaseStation) {
        return coverageRadiusLeftBorder != null ?
                qBaseStation.coverageRadius.goe(coverageRadiusLeftBorder)
                : null;
    }

    @Override
    public List<BaseStation> baseStations() {
        return baseStationsRepository.findAll();
    }

    @Override
    public BaseStation baseStation(Integer baseStationId) throws NotFoundException {
        return baseStationsRepository
                .findById(baseStationId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void remove(Integer baseStationId) {
        baseStationsRepository.deleteById(baseStationId);
    }

    @Override
    public BaseStation save(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }

    @Override
    public BaseStation update(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }
}
