package ru.cifrak.telecomit.backend.api;

import org.springframework.data.jpa.domain.Specification;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.AccessPointFull_;

public class PointSpecification  {

    public static Specification<AccessPointFull> empty() {
        return Specification.where(null);
    }

    public static Specification<AccessPointFull> search(String address) {
        return (entity, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
//                        criteriaBuilder.like(criteriaBuilder.lower(entity.get(AccessPointFull_.organization).get(Organization_.address)), "%" + address + "%")
                        criteriaBuilder.like(criteriaBuilder.lower(entity.get(String.valueOf(AccessPointFull_.organization))), "%" + address + "%")
                );
    }
}

