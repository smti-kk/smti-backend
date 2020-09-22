package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LocationFeaturesEditingRequest {
    @Id
    @SequenceGenerator(name = "LOCATION_FEATURES_EDITING_GENERATOR", sequenceName = "location_features_editing_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_FEATURES_EDITING_GENERATOR")
    private Integer id;

    @Column(name = "location_id")
    private Integer locationId;
    private String comment;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<FeatureEdit> featureEdits;

    @Enumerated(EnumType.STRING)
    private EditingRequestStatus status = WAIT_FOR_STATE_TO_BE_SET;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime modified;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    private String declineComment;

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

    public void decline(String comment) {
        setDeclineComment(comment);
        setStatus(DECLINED);
    }
}

