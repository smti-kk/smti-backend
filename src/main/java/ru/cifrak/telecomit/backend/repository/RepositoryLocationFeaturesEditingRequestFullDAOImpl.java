package ru.cifrak.telecomit.backend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditFull;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Repository
public class RepositoryLocationFeaturesEditingRequestFullDAOImpl implements RepositoryLocationFeaturesEditingRequestFullDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int deleteRowsInJournalByTerm(int years) {
        AtomicInteger result = new AtomicInteger();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<LocationFeaturesEditingRequestFull> requests = getRequests(years, cb);
        requests.forEach(request -> {
            Set<FeatureEditFull> featureEdits = request.getFeatureEdits();
            result.addAndGet(deleteEntity(LocationFeaturesEditingRequestFull.class, cb, request));
            featureEdits.forEach(featureEdit -> deleteEntity(FeatureEditFull.class, cb, featureEdit));
        });
        return result.get();
    }

    private <T> int deleteEntity(Class<T> type, CriteriaBuilder cb, T entity) {
        CriteriaDelete<T> cd = cb.createCriteriaDelete(type);
        Root<T> root = cd.from(type);
        cd.where(cb.equal(root, entity));
        return entityManager.createQuery(cd).executeUpdate();
    }

    /**
     * Execute query, that select requests with created is older than minus specified value of years.
     * For example:
     *  years == 2,
     *  now == 2022-02-28T00:00   ->   created < 2020-02-29T00:00
     *  now == 2024-02-29T00:00   ->   created < 2022-03-01T00:00
     *  now == 2022-05-15T00:00   ->   created < 2020-05-16T00:00
     *
     * @param years years to minus from now for condition clause
     * @param cb criteria builder
     * @return requests with created is older than minus specified value of years
     */
    private List<LocationFeaturesEditingRequestFull> getRequests(int years, CriteriaBuilder cb) {
        CriteriaQuery<LocationFeaturesEditingRequestFull> cq = cb.createQuery(LocationFeaturesEditingRequestFull.class);
        Root<LocationFeaturesEditingRequestFull> root = cq.from(LocationFeaturesEditingRequestFull.class);
        Predicate predicate = cb.lessThan(
                root.get(LocationFeaturesEditingRequestFull_.created),
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusYears(years).plusDays(1)
        );
        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }
}
