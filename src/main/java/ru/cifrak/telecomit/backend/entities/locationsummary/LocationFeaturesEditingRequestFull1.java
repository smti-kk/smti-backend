package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import ru.cifrak.telecomit.backend.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.cifrak.telecomit.backend.entities.locationsummary.EditingRequestStatus.WAIT_FOR_STATE_TO_BE_SET;

@Data
@Entity
@Table(name = "location_features_editing_request")
public class LocationFeaturesEditingRequestFull1 {
    @Id
    @SequenceGenerator(name = "LOCATION_FEATURES_EDITING_GENERATOR", sequenceName = "location_features_editing_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_FEATURES_EDITING_GENERATOR")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationForTable location;

    private String comment;

    private String declineComment;

    @ManyToOne
    private User user;

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

    @Enumerated(EnumType.STRING)
    @Column
    private ChangeSource changeSource;
}
