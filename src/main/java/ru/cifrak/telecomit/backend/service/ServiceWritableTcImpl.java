package ru.cifrak.telecomit.backend.service;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import java.time.Year;
import java.util.List;
import java.util.Set;

@Service
public class ServiceWritableTcImpl implements ServiceWritableTc {
    private final RepositoryWritableTc repositoryWritableTc;

    public ServiceWritableTcImpl(RepositoryWritableTc repositoryWritableTc) {
        this.repositoryWritableTc = repositoryWritableTc;
    }

    @Override
    public void editLocationFeatures(Set<FeatureEdit> features, Integer locationId) {
        features.forEach(f -> {
            switch (f.getAction()) {
                case UPDATE:
                    updateAndSave(f.getTc(), f.getNewValue());
                    break;
                case CREATE:
                    createNewAndSave(f.getTc(), locationId);
                    break;
                case DELETE:
                    moveToArchiveAndSave(f.getTc());
                    break;
            }
        });
    }

    @Override
    public Set<FeatureEdit> defineEditActions(Set<WritableTc> features, Integer locationId) {
        List<WritableTc> existActiveLocationFeatures = repositoryWritableTc.findAllByLocationIdAndState(
                locationId,
                TcState.ACTIVE
        );
        return new FeatureEditHashSet(features, existActiveLocationFeatures);
    }

    public void moveToArchiveAndSave(WritableTc writableTc) {
        writableTc.setState(TcState.ARCHIVE);
        repositoryWritableTc.save(writableTc);
    }

    public void updateAndSave(WritableTc previousValue, WritableTc newValue) {
        if (newValue.isPlan()) {
            newValue.setState(TcState.PLAN);
        } else {
            newValue.setState(TcState.ACTIVE);
            previousValue.setState(TcState.ARCHIVE);
        }
        repositoryWritableTc.save(previousValue);
        repositoryWritableTc.save(newValue);
    }

    public void createNewAndSave(WritableTc newTc, Integer locationId) {
        if (newTc.isPlan()) {
            newTc.setState(TcState.PLAN);
        } else {
            newTc.setState(TcState.ACTIVE);
        }
        newTc.setLocationId(locationId);
        repositoryWritableTc.save(newTc);
    }
}
