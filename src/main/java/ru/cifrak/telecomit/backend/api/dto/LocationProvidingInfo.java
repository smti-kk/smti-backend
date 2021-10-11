package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.map.TechnicalCapabilityForLocationTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class LocationProvidingInfo {
    private String name;
    private String type;
    private Integer population;
    private Integer locationsCount;
    private FeatureProvidingInfo cellular;
    private FeatureProvidingInfo internet;
    private FeatureProvidingInfo tv;
    private FeatureProvidingInfo payphone;
    private FeatureProvidingInfo ats;
    private FeatureProvidingInfo radio;
    private FeatureProvidingInfo post;
    private FeatureProvidingInfo infomat;

    public LocationProvidingInfo(LocationForTable location) {
        List<LocationForTable> childs = new ArrayList<>(geChilds(location));
        AtomicReference<Integer> population = new AtomicReference<>(0);
        childs.forEach(c -> {
            population.set(population.get() + c.getPopulation());
        });
        name = location.getName();
        type = location.getType();
        this.population = population.get();
        locationsCount = childs.size();
        cellular = featureProvidingInfo(childs, population.get(), "MOBILE");
        internet = featureProvidingInfo(childs, population.get(), "INET");
        tv = featureProvidingInfo(childs, population.get(), "TV");
        payphone = featureProvidingInfo(childs, population.get(), "PAYPHONE");
        ats = featureProvidingInfo(childs, population.get(), "ATS");
        radio = featureProvidingInfo(childs, population.get(), "RADIO");
        post = featureProvidingInfo(childs, population.get(), "POST");
        infomat = featureProvidingInfo(childs, population.get(), "INFOMAT");
    }

    private FeatureProvidingInfo featureProvidingInfo(List<LocationForTable> locations,
                                                      Integer population,
                                                      String type) {
        final Double[] countProvided = {0.0};
        final Double[] populationsProvided = {0.0};
        locations.forEach(l -> {
            if (locationIsProvided(l, type)) {
                countProvided[0] = countProvided[0] + 1;
                populationsProvided[0] = populationsProvided[0] + l.getPopulation();
            }
        });
        return new FeatureProvidingInfo(
                countProvided[0].intValue(),
                populationsProvided[0].intValue(),
                (int) (countProvided[0] / locations.size() * 100),
                (int) (populationsProvided[0] / population * 100)
        );
    }

    private List<LocationForTable> geChilds(LocationForTable location) {
        List<LocationForTable> childs = new ArrayList<>();
        if (location.getPopulation() != 0 && location.getPopulation() != null) {
            if (!"р-н".equals(location.getType())
                    && !"край".equals(location.getType())
                    && !"с/с".equals(location.getType())
                    && !"тер".equals(location.getType())
                    && !"округ".equals(location.getType())
            ) {
                childs.add(location);
            }
        }
        location.getChildren().forEach(c -> {
            if (c.getChildren().isEmpty()) {
                if (!"р-н".equals(c.getType())
                        && !"край".equals(c.getType())
                        && !"с/с".equals(c.getType())
                        && !"тер".equals(c.getType())
                        && !"округ".equals(c.getType())
                ) {
                    childs.add(c);
                }
            } else {
                childs.addAll(geChilds(c));
            }
        });
        return childs;
    }

    private boolean locationIsProvided(LocationForTable location, String type) {
        for (TechnicalCapabilityForLocationTable tc : location.getTechnicalCapabilities()) {
            if (tc.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
