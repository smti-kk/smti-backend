package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeature;

import javax.persistence.*;

@Entity
@Table(name = "feature_edit")
@Data
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = FeatureEditFull.FULL
        )
})
public class FeatureEditFull {
    public static final String FULL = "FeatureEditFull.FULL";

    @Id
    @SequenceGenerator(name = "FEATURE_EDIT_GENERATOR", sequenceName = "features_edit_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FEATURE_EDIT_GENERATOR")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private FeatureEditAction action;

    @ManyToOne(cascade = CascadeType.MERGE)
    private LocationFeature tc;

    @ManyToOne(cascade = CascadeType.MERGE)
    private LocationFeature newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "location_features_editing_request_feature_edits",
            joinColumns = @JoinColumn(name = "feature_edits_id", referencedColumnName="ID"),
            inverseJoinColumns = @JoinColumn(name = "location_features_editing_request_id", referencedColumnName="ID")
    )
    private LocationFeaturesEditingRequestFull1 locationFeaturesEditingRequest;
}
