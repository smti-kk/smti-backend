package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;

@Data
public class ExelReportAccessPointFullDTO {
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
    private Boolean pointView;
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


    public ExelReportAccessPointFullDTO(AccessPointFull item) {
        this.idOrg = item.getOrganization().getId();
        this.contract = item.getType().equals(TypeAccessPoint.CONTRACT) ? "№" + item.getNumber() + ", сумма; "  + item.getAmount() + "р." : "";
        this.locationType = item.getOrganization().getLocation().getType();
        this.locationName = item.getOrganization().getLocation().getName();
        this.OKTMO = item.getOrganization().getLocation().getOktmo();
        this.numberInhabitants = item.getOrganization().getLocation().getPopulation();
        this.fullNameOrganization = item.getOrganization().getName();
        this.FIASOrganization = item.getOrganization().getFias().toString();
        this.fullAddressOrganization = item.getOrganization().getAddress();
        this.latitude = item.getPoint().getX();
        this.longitude = item.getPoint().getY();
        // доделат СЗО!
        this.pointView = item.getVisible();
        this.accessPointCustomer = item.getCustomer();
        // колонки с r по t
        this.contract = item.getType().equals(TypeAccessPoint.CONTRACT) ? "№" + item.getNumber() + ", сумма; "  + item.getAmount() + "р." : "";
        this.ucn = item.getUcn();
        this.accessNode = "";
        this.SMO = item.getOrganization().getSmo()!=null?item.getOrganization().getSmo().getName():"";
        //тип подключения
        this.operatorName = item.getContractor();
        this.declaredSpeed = item.getDeclaredSpeed();
        this.channelWidth = null;
        this.descriptionAccess = item.getDescription();
        this.yearOverGovProgram = item.getCompleted();
        this.participationStatus = "";
        this.communicationAssessment = item.getQuality();
        this.includeType = item.getType() != null ? item.getType().getName() : "";
        this.companyType = item.getOrganization().getType()!=null? item.getOrganization().getType().getName() : "";
//        this.municipalName = (item.getOrganization().getLocation().getParent().getName() != null) ? item.getOrganization().getLocation().getParent().getName() : "что то" ;
        this.municipalName = "";
        //this.governmentProgramName = item.getGovernmentDevelopmentProgram().getName();
        this.governmentProgramName = "";
    }
}
