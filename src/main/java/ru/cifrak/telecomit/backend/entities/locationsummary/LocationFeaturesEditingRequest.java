package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import javax.persistence.*;
import java.util.Set;

import static ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus.ACCEPTED;
import static ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus.WAIT_FOR_STATE_TO_BE_SET;

@Data
@Entity
public class LocationFeaturesEditingRequest {
    @Id
    @SequenceGenerator(name = "LOCATION_FEATURES_EDITING_GENERATOR", sequenceName = "location_features_editing_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_FEATURES_EDITING_GENERATOR")
    private Integer id;
    private Integer locationId;
    private String comment;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<FeatureEdit> featureEdits;

    @Enumerated(EnumType.STRING)
    private EditingRequestStatus status = WAIT_FOR_STATE_TO_BE_SET;

    public LocationFeaturesEditingRequest(Integer locationId,
                                          String comment,
                                          User user,
                                          Set<FeatureEdit> features) {
        this.locationId = locationId;
        this.comment = comment;
        this.user = user;
        this.featureEdits = features;
    }

    public LocationFeaturesEditingRequest() {
    }

    public void accept(ServiceWritableTc service) {
        setStatus(ACCEPTED);
        service.editLocationFeatures(featureEdits, locationId);
    }
}

