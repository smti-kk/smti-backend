package ru.cifrak.telecomit.backend.repository;

import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.AccessPoint_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccessPointDAOImpl implements AccessPointDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findAllAddressesByOccurrence(String stringOccurrence) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<AccessPoint> root = query.from(AccessPoint.class);
        Predicate predicate = builder.like(
                builder.lower(root.get(AccessPoint_.address)),
                "%" + stringOccurrence.toLowerCase() + "%");
        query.where(predicate);
        query.multiselect(root.get(AccessPoint_.address));
        List<Tuple> tupleResult = entityManager.createQuery(query).setMaxResults(10).getResultList();
        List<String> resultStringList = new ArrayList<>();
        tupleResult.forEach(t -> resultStringList.add((String) t.get(0)));
        return resultStringList;
    }
}
