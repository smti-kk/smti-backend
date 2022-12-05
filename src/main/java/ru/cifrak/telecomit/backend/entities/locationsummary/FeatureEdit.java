package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeatureAp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class FeatureEdit {

    @Id
    @SequenceGenerator(name = "FEATURE_EDIT_GENERATOR", sequenceName = "features_edit_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FEATURE_EDIT_GENERATOR")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private FeatureEditAction action;

    @ManyToOne(cascade = CascadeType.MERGE)
    private WritableTc tc;

    @ManyToOne(cascade = CascadeType.MERGE)
    private WritableTc newValue;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "access_point_id")
    private LocationFeatureAp accessPoint;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "new_access_point_id")
    private LocationFeatureAp newAccessPoint;

    public FeatureEdit() {
    }

    public FeatureEdit(@NotNull WritableTc tc, @NotNull FeatureEditAction action) {
        if (action == FeatureEditAction.UPDATE) {
            throw new RuntimeException("UPDATE REQUIRE NEW VALUE");
        }
        this.tc = tc;
        this.action = action;
    }

    public FeatureEdit(@NotNull WritableTc tc, @NotNull WritableTc newValue) {
        this.tc = tc;
        this.newValue = newValue;
        this.action = FeatureEditAction.UPDATE;
    }

    public FeatureEdit(@NotNull LocationFeatureAp accessPoint,
                       @NotNull FeatureEditAction action) {
        this.accessPoint = accessPoint;
        this.action = action;
    }

    public FeatureEdit(@NotNull LocationFeatureAp accessPoint,
                       @NotNull LocationFeatureAp newAccessPoint) {
        this.accessPoint = accessPoint;
        this.newAccessPoint = newAccessPoint;
        this.action = FeatureEditAction.UPDATE;
    }

}
