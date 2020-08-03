package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import java.util.List;

@RestController
public class FeaturesComparingApiImpl implements FeaturesComparingApi {

    private final LocationRepository locationRepository;
    private final RepositoryWritableTc repositoryWritableTc;

    public FeaturesComparingApiImpl(LocationRepository locationRepository,
                                    RepositoryWritableTc repositoryWritableTc) {
        this.locationRepository = locationRepository;
        this.repositoryWritableTc = repositoryWritableTc;
    }

    @Override
    public List<LocationFC> locations() {
        return locationRepository.findAll();
    }

    @Override
    public void makeItActive(Integer locationId, Integer featureId) {
        WritableTc feature = repositoryWritableTc.getOne(featureId);
        WritableTc activeFeature = repositoryWritableTc.findByLocationIdAndStateAndOperatorId(
                locationId,
                TcState.ACTIVE,
                feature.getOperatorId()
        );
        feature.setState(TcState.ACTIVE);
        if (activeFeature != null) {
            activeFeature.setState(TcState.ARCHIVE);
            repositoryWritableTc.save(activeFeature);
        }
        repositoryWritableTc.save(feature);
    }

}
