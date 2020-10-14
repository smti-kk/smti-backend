package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;

import java.util.List;

@Repository
public interface RepositoryFeaturesRequests extends JpaRepository<LocationFeaturesEditingRequestFull, Integer> {

    @Query("FROM LocationFeaturesEditingRequestFull r " +
            "WHERE r.comment not like '%Отредактировано%' " +
            "AND r.comment not like '%Импорт%' " +
            "ORDER BY r.created DESC")
    Page<LocationFeaturesEditingRequestFull> findAllRequests(Pageable pageable);
    @Query("FROM LocationFeaturesEditingRequestFull r " +
            "WHERE r.status <> 'DECLINED' " +
            "AND r.status <> 'WAIT_FOR_STATE_TO_BE_SET' " +
            "ORDER BY r.created DESC")
    Page<LocationFeaturesEditingRequestFull> findAllRequestsAndImportAndEditions(Pageable pageable);
    List<LocationFeaturesEditingRequestFull> findAllByLocationIdOrderByCreatedDesc(Integer locationId);
    Page<LocationFeaturesEditingRequestFull> findAllByUserOrderByCreatedDesc(User user, Pageable pageable);
}
