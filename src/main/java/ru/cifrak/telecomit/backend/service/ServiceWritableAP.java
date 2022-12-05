package ru.cifrak.telecomit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.ApESPD;
import ru.cifrak.telecomit.backend.entities.ApSMO;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeatureAp;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceWritableAP implements ServiceWritable {

    private final RepositoryAccessPoints repositoryAccessPoints;

    @Override
    public void editLocationFeatures(Set<FeatureEdit> features, Integer locationId) {
        features.forEach(f -> {
            AccessPoint accessPoint = f.getAccessPoint().convertToAccessPoint();
            switch (f.getAction()) {
                case UPDATE:
                    this.updateAndSave(accessPoint,
                            f.getNewAccessPoint().convertToAccessPoint());
                    break;
                case CREATE:
                    this.createNewAndSave(accessPoint);
                    break;
                case DELETE:
                    this.moveToTrashAndSave(accessPoint);
                    break;
            }
        });
    }

    @Override
    public void editMunicipalityLocationFeatures(Set<FeatureEdit> features, Integer locationId) {

    }

    @Override
    public Set<FeatureEdit> defineEditActions(Set<WritableTc> features, Integer locationId) {
        return null;
    }



    private void createNewAndSave(AccessPoint ap) {
        this.changeModeAccessPoint(ap, true);
        ap.setMaxAmount(0);
        repositoryAccessPoints.save(ap);
    }

    private void updateAndSave(AccessPoint ap, AccessPoint newAp) {
        this.changeModeAccessPoint(ap, false);
        repositoryAccessPoints.save(newAp);
        repositoryAccessPoints.save(ap);
    }

    private void moveToTrashAndSave(AccessPoint ap) {
        this.changeModeAccessPoint(ap, false);
        repositoryAccessPoints.save(ap);
    }

    private void changeModeAccessPoint(AccessPoint ap, boolean isOn) {
        ap.setVisible(isOn);
        ap.setDeleted(!isOn);
    }

}
