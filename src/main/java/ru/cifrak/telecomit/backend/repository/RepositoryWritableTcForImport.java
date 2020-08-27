package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;

import java.util.List;

@Repository
public interface RepositoryWritableTcForImport extends JpaRepository<WritableTcForImport, Integer> {
    List<WritableTcForImport> findAllByLocationIdAndState(Integer locationId, TcState state);

    @Query("SELECT DISTINCT tc.govYearComplete FROM WritableTcForImport tc where tc.govYearComplete is not NULL")
    List<Integer> existGovCompleteYears();

    WritableTcForImport findByLocationIdAndStateAndOperatorId(Integer locationId, TcState state, Integer operatorId);

    List<WritableTcForImport> findByLocationIdAndOperatorIdAndType(
            Integer locationId,
            Integer operatorId,
            String type
    );
}
