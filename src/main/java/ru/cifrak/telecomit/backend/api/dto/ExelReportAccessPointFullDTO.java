package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Data
public class ExelReportAccessPointFullDTO {
    private final static String ERROR_DATA_STRING = "Некоректные даные" ;
    private final static Integer ERROR_DATA_INTEGER = -1;
    private final static Integer COMPLETE_NULL_INTEGER = null;
    private final static String NOTHING_TO_SAY = "";
    private Integer pp;

    private Integer id;

    /*
        Organization Info
    */
    private Integer idOrg;
    private String municipalLocationType;//!!!
    private String municipalName;//!!!
    private String locationType;//!!!
    private String locationName;//!!!
    private String OKTMO;
    private Integer numberInhabitants;
    private String fullNameOrganization;
    private String FIASOrganization;
    private String fullAddressOrganization;
    private Double latitude;
    private Double longitude;
    private String pointView;
    private String accessPointCustomer;
    private String operatorName;
    private String declaredSpeed;
    private String channelWidth;
    private String descriptionAccess;
    private String governmentProgramName;
    private String contract;
    private Integer yearOverGovProgram;
    private Integer ucn;
    private String accessNode;
    private String SMO;
    private String participationStatus;
    private String communicationAssessment;
    private String includeType;
    private String companyType;
    private Long dayTraffic;
    private String monitoring;
    private String connectionState;
    private String problem;
    private ImportanceProblemStatus importance;
    private Date createDate;
    private String problemDefinition;

    public ExelReportAccessPointFullDTO(AccessPointFull item, User user) {
        this.idOrg = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getId).orElse(ERROR_DATA_INTEGER);
        this.municipalLocationType = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getParent).map(Location::getType).orElse(ERROR_DATA_STRING);
        this.municipalName = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getParent).map(Location::getName).orElse(ERROR_DATA_STRING);
        this.locationType = item.getOrganization() != null ? item.getOrganization().getLocation() != null ? item.getOrganization().getLocation().getType() : ERROR_DATA_STRING : ERROR_DATA_STRING ;
        this.locationName = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getName).orElse(ERROR_DATA_STRING);
        this.OKTMO = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getOktmo).orElse(NOTHING_TO_SAY);
        this.numberInhabitants = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getPopulation).orElse(ERROR_DATA_INTEGER);
        this.fullNameOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getName).orElse(ERROR_DATA_STRING);
        this.FIASOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getFias).map(UUID::toString).orElse(ERROR_DATA_STRING);
        this.fullAddressOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getAddress).orElse(ERROR_DATA_STRING);
        this.latitude = Optional.ofNullable(item).map(AccessPointFull::getPoint).map(Point::getX).orElse(Double.valueOf(ERROR_DATA_INTEGER));
        this.longitude = Optional.ofNullable(item).map(AccessPointFull::getPoint).map(Point::getY).orElse(Double.valueOf(ERROR_DATA_INTEGER));
        this.SMO = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getSmo).map(TypeSmo::getName).orElse(ERROR_DATA_STRING);
        this.companyType = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getType).map(TypeOrganization::getName).orElse(ERROR_DATA_STRING);
        this.pointView = convertToRuLoc(Optional.ofNullable(item).map(AccessPointFull::getVisible).orElse(false));
        this.accessPointCustomer = Optional.ofNullable(item).map(AccessPointFull::getCustomer).orElse(NOTHING_TO_SAY);
        //ToDo:contract!
//        this.contract = item.getType().equals(TypeAccessPoint.CONTRACT) ? "№ "
//                + (item.getNumber() == null ? "" : item.getNumber()) + ", сумма; "
//                + (item.getAmount() == null ? 0 : item.getAmount() / 100) + "р." : "";
        //ToDo:accessNode
        this.accessNode = "";
        this.descriptionAccess = Optional.ofNullable(item).map(AccessPointFull::getDescription).orElse(ERROR_DATA_STRING);
        this.includeType = Optional.ofNullable(item).map(AccessPointFull::getInternetAccess).map(TypeInternetAccess::getName).orElse(ERROR_DATA_STRING);
        this.operatorName = item.getContractor();
        this.declaredSpeed = Optional.ofNullable(item).map(AccessPointFull::getDeclaredSpeed).orElse(ERROR_DATA_STRING);
        this.channelWidth = null;
        this.communicationAssessment = Optional.ofNullable(item).map(AccessPointFull::getQuality).orElse(NOTHING_TO_SAY);
        this.ucn = Optional.ofNullable(item).map(AccessPointFull::getUcn).orElse(COMPLETE_NULL_INTEGER);
        this.governmentProgramName = Optional.ofNullable(item).map(AccessPointFull::getGovernmentDevelopmentProgram).map(GovernmentDevelopmentProgram::getName).orElse(NOTHING_TO_SAY);
        this.participationStatus = "";
        this.yearOverGovProgram = Optional.ofNullable(item).map(AccessPointFull::getCompleted).orElse(COMPLETE_NULL_INTEGER);
        if (user.getRoles().contains(UserRole.CONTRACTOR)) {
            Long trafficLocal = Optional.ofNullable(item.getMonitoringLink()).map(JournalMAP::getMap)
                    .map(MonitoringAccessPoint::getLastDayTraffic)
                    .orElse(0L);
            if (trafficLocal != 0) {
                this.dayTraffic = trafficLocal;
            }
            APConnectionState state = Optional.ofNullable(item.getMonitoringLink())
                    .map(JournalMAP::getMap)
                    .map(MonitoringAccessPoint::getConnectionState)
                    .orElse(APConnectionState.NOT_MONITORED);
            this.connectionState = state.toString();
            if (state != APConnectionState.NOT_MONITORED) {
                this.monitoring = "Да" ;
            }
            boolean problemLocal = state == APConnectionState.PROBLEM;
            if (problemLocal) {
                this.problem = "Да";
            }
            this.importance = Optional.ofNullable(item.getMonitoringLink()).map(JournalMAP::getMap)
                    .map(MonitoringAccessPoint::getImportance)
                    .orElse(null);
            LocalDateTime createdDateTime = Optional.ofNullable(item.getMonitoringLink())
                    .map(JournalMAP::getMap)
                    .map(MonitoringAccessPoint::getCreateDatetime)
                    .orElse(null);
            if (createdDateTime != null) {
                this.createDate = new Date(createdDateTime.getYear() - 1900,
                        createdDateTime.getMonthValue() - 1,
                        createdDateTime.getDayOfMonth());
            } else {
                this.createDate = null;
            }
            this.problemDefinition = Optional.ofNullable(item.getMonitoringLink())
                    .map(JournalMAP::getMap)
                    .map(MonitoringAccessPoint::getProblemDefinition)
                    .orElse("");
        }
    }

    public ExelReportAccessPointFullDTO(AccessPointFull item) {
        this.idOrg = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getId).orElse(ERROR_DATA_INTEGER);
        this.municipalLocationType = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getParent).map(Location::getType).orElse(ERROR_DATA_STRING);
        this.municipalName = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getParent).map(Location::getName).orElse(ERROR_DATA_STRING);
        this.locationType = item.getOrganization() != null ? item.getOrganization().getLocation() != null ? item.getOrganization().getLocation().getType() : ERROR_DATA_STRING : ERROR_DATA_STRING ;
        this.locationName = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getName).orElse(ERROR_DATA_STRING);
        this.OKTMO = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getOktmo).orElse(NOTHING_TO_SAY);
        this.numberInhabitants = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getLocation).map(Location::getPopulation).orElse(ERROR_DATA_INTEGER);
        this.fullNameOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getName).orElse(ERROR_DATA_STRING);
        this.FIASOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getFias).map(UUID::toString).orElse(ERROR_DATA_STRING);
        this.fullAddressOrganization = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getAddress).orElse(ERROR_DATA_STRING);
        this.latitude = Optional.ofNullable(item).map(AccessPointFull::getPoint).map(Point::getX).orElse(Double.valueOf(ERROR_DATA_INTEGER));
        this.longitude = Optional.ofNullable(item).map(AccessPointFull::getPoint).map(Point::getY).orElse(Double.valueOf(ERROR_DATA_INTEGER));
        this.SMO = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getSmo).map(TypeSmo::getName).orElse(ERROR_DATA_STRING);
        this.companyType = Optional.ofNullable(item).map(AccessPointFull::getOrganization).map(Organization::getType).map(TypeOrganization::getName).orElse(ERROR_DATA_STRING);
        this.pointView = convertToRuLoc(Optional.ofNullable(item).map(AccessPointFull::getVisible).orElse(false));
        this.accessPointCustomer = Optional.ofNullable(item).map(AccessPointFull::getCustomer).orElse(NOTHING_TO_SAY);
        //ToDo:contract!
//        this.contract = item.getType().equals(TypeAccessPoint.CONTRACT) ? "№ " + item.getNumber() + ", сумма; "  + item.getAmount() / 100 + "р." : "";
        //ToDo:accessNode
        this.accessNode = "";
        this.descriptionAccess = Optional.ofNullable(item).map(AccessPointFull::getDescription).orElse(ERROR_DATA_STRING);
        this.includeType = Optional.ofNullable(item).map(AccessPointFull::getInternetAccess).map(TypeInternetAccess::getName).orElse(ERROR_DATA_STRING);
        this.operatorName = item.getContractor();
        this.declaredSpeed = Optional.ofNullable(item).map(AccessPointFull::getDeclaredSpeed).orElse(ERROR_DATA_STRING);
        this.channelWidth = null;
        this.communicationAssessment = Optional.ofNullable(item).map(AccessPointFull::getQuality).orElse(NOTHING_TO_SAY);
        this.ucn = Optional.ofNullable(item).map(AccessPointFull::getUcn).orElse(COMPLETE_NULL_INTEGER);
        this.governmentProgramName = Optional.ofNullable(item).map(AccessPointFull::getGovernmentDevelopmentProgram).map(GovernmentDevelopmentProgram::getName).orElse(NOTHING_TO_SAY);
        this.participationStatus = "";
        this.yearOverGovProgram = Optional.ofNullable(item).map(AccessPointFull::getCompleted).orElse(COMPLETE_NULL_INTEGER);
    }

    private static String convertToRuLoc(Boolean pointView) {
        return pointView.equals(true) ? "Да" : "Нет";
    }
}
