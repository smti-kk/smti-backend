package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import java.util.List;

@RestController
public class FeaturesComparingApiImpl implements FeaturesComparingApi {

    private final LocationRepository locationRepository;
    private final FeatureComparingService featureComparingService;
    private final RepositoryWritableTc repositoryWritableTc;

    public FeaturesComparingApiImpl(LocationRepository locationRepository,
                                    FeatureComparingService featureComparingService,
                                    RepositoryWritableTc repositoryWritableTc) {
        this.locationRepository = locationRepository;
        this.featureComparingService = featureComparingService;
        this.repositoryWritableTc = repositoryWritableTc;
    }

    @Override
    public Page<LocationFC> locations(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    @Override
    public Page<LocationFC> locationsInet(
            Pageable pageable,
            List<Integer> parentIds,
            String locationName,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAnyInternet,
            String type
    ) {
        if (type.equals("INET")) {
            return featureComparingService.locations(
                    pageable,
                    parentIds,
                    locationName,
                    operators,
                    null,
                    connectionTypes,
                    null,
                    govProgram,
                    govProgramYear,
                    hasAnyInternet,
                    null
            );
        } else if (type.equals("MOBILE")) {
            return featureComparingService.locations(
                    pageable,
                    parentIds,
                    locationName,
                    null,
                    operators,
                    null,
                    connectionTypes,
                    govProgram,
                    govProgramYear,
                    null,
                    hasAnyInternet
            );
        } else {
            return null;
        }
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
