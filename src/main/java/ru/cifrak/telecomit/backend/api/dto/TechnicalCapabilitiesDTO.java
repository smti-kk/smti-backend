package ru.cifrak.telecomit.backend.api.dto;

import lombok.Getter;
import ru.cifrak.telecomit.backend.domain.*;

import java.util.Set;

@Getter
public class TechnicalCapabilitiesDTO {
    private Set<FtcInternet> internet;
    private Set<FtcAts> ats;
    private Set<FtcMobile> cellular;
    private Set<FtcPost> post;
    private Set<FtcRadio> radio;
    private Set<FtcTelevision> television;
    private LocationSimple location;

    public TechnicalCapabilitiesDTO(CatalogsLocation location) {
        this.internet = location.getFtcInternets();
        this.ats = location.getFtcAts();
        this.cellular = location.getFtcMobiles();
        this.post = location.getFtcPosts();
        this.radio = location.getFtcRadios();
        this.television = location.getFtcTelevisions();
        this.location = new LocationSimple(location);
    }
}
