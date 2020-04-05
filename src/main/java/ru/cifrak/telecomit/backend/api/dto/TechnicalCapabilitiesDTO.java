package ru.cifrak.telecomit.backend.api.dto;

import lombok.Getter;
import ru.cifrak.telecomit.backend.domain.*;

import java.util.List;

@Getter
public class TechnicalCapabilitiesDTO {
    private List<FtcInternet> internet;
    private List<FtcAts> ats;
    private List<FtcMobile> cellular;
    private List<FtcPost> post;
    private List<FtcRadio> radio;
    private List<FtcTelevision> television;
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
