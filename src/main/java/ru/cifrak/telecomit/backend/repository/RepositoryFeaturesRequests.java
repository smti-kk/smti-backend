package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;

import java.util.List;

@Repository
public interface RepositoryFeaturesRequests extends JpaRepository<LocationFeaturesEditingRequestFull, Integer> {
    List<LocationFeaturesEditingRequestFull> findAllByOrderByCreatedDesc();
    List<LocationFeaturesEditingRequestFull> findAllByLocationIdOrderByCreatedDesc(Integer locationId);
    List<LocationFeaturesEditingRequestFull> findAllByUserOrderByCreatedDesc(User user);
}
