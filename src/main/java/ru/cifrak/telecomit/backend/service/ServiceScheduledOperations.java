package ru.cifrak.telecomit.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesEditingRequestFullDAO;

@Slf4j
@Service
public class ServiceScheduledOperations {
    private static final int YEARS_FOR_REMOVE_REQUESTS = 2;
    private final RepositoryLocationFeaturesEditingRequestFullDAO repositoryLocationFeaturesEditingRequestFullDAO;

    @Autowired
    public ServiceScheduledOperations(
            RepositoryLocationFeaturesEditingRequestFullDAO repositoryLocationFeaturesEditingRequestFullDAO) {
        this.repositoryLocationFeaturesEditingRequestFullDAO = repositoryLocationFeaturesEditingRequestFullDAO;
    }

    @Scheduled(cron = "0 0 0 * * *")    //every midnight
    public void deleteRowsInJournalOlderThanNeededYears() {
        log.info("<-- delete older rows in journal (location_features_editing_request)");
        int countDeletedRows =
                repositoryLocationFeaturesEditingRequestFullDAO.deleteRowsInJournalByTerm(YEARS_FOR_REMOVE_REQUESTS);
        log.info("--> delete older rows in journal (location_features_editing_request): {} rows", countDeletedRows);
    }
}
