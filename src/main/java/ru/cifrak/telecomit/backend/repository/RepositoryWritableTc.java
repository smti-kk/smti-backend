package ru.cifrak.telecomit.backend.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

import java.util.List;

@Repository
public interface RepositoryWritableTc extends JpaRepository<WritableTc, Integer> {
    List<WritableTc> findAllByLocationIdAndState(Integer locationId, TcState state);

    @Cacheable("gov_years")
    @Query("SELECT DISTINCT tc.govYearComplete FROM WritableTc tc where tc.govYearComplete is not NULL")
    List<Integer> existGovCompleteYears();

    WritableTc findByLocationIdAndStateAndOperatorIdAndType(Integer locationId, TcState state, Integer operatorId, String type);

//    List<WritableTc> findByLocationIdAndOperatorIdAndType(
//            Integer locationId,
//            Integer operatorId,
//            String type
//    );

    List<WritableTc> findByLocationIdAndOperatorIdAndType(
            Integer locationId,
            Integer operatorId,
            String type
    );
}
