/*
package ru.cifrak.telecomit.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcPost;

import java.util.List;

public interface RepositoryFtcPost extends JpaRepository<FtcPost, Integer> {
    @Query(value = "SELECT fi from FtcPost fi where fi.catalogsLocation.id = ?1")
    List<FtcPost> getByLocationId(Integer locationId);
}
*/
