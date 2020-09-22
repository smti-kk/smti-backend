package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;

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
}
