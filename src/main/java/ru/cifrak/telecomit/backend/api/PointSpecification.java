package ru.cifrak.telecomit.backend.api;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.AccessPoint_;

public class PointSpecification  {

    public static Specification<AccessPointFull> empty() {
        return Specification.where(null);
    }

    public static Specification<AccessPoint> search(String address) {
        return (entity, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(entity.get(AccessPoint_.address)),  address.toLowerCase() + "%"));
    }
}

