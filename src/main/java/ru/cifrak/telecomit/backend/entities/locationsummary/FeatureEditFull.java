package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeature;

import javax.persistence.*;

@Entity
@Table(name = "feature_edit")
@Data
public class FeatureEditFull {
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
}
