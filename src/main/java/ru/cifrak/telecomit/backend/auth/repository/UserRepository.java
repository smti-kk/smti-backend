package ru.cifrak.telecomit.backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.auth.entity.User;
import ru.cifrak.telecomit.backend.auth.entity.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOid(Long oid);
    Optional<User> findByUsername(String phone);
    Optional<User> findByPhone(String phone);

    List<User> findByRolesInAndIsActiveTrue(Set<UserRole> roles);

    // Для выгрузки
    List<User> findAllByCreateDateTimeGreaterThanEqualAndCreateDateTimeLessThanOrderByIdAsc(
            LocalDateTime from, LocalDateTime to);
}
