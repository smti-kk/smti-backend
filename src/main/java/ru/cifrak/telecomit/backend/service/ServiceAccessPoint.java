package ru.cifrak.telecomit.backend.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.AccessPointDetailInOrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.AccessPointNewDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.*;

@Transactional
@Service
public class ServiceAccessPoint {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryLocation rLocation;
    private final RepositorySmoType rTypeSmo;
    private final RepositoryOrganizationType rTypeOrganization;

    public ServiceAccessPoint(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, RepositoryLocation rLocation, RepositorySmoType rTypeSmo, RepositoryOrganizationType rTypeOrganization) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rLocation = rLocation;
        this.rTypeSmo = rTypeSmo;
        this.rTypeOrganization = rTypeOrganization;
    }

    public AccessPointDetailInOrganizationDTO giveNewCreatedAccessPoint(@NotNull final Organization organization, @NotNull final AccessPointNewDTO dto) throws Exception {
        switch (dto.getType()) {
            case "SMO":
                final ApSMO smo = new ApSMO();
                initializeWithCommonFields(smo, organization, dto);
                rAccessPoints.save(smo);
                return new AccessPointDetailInOrganizationDTO(smo);
            case "ESPD":
                final ApESPD espd = new ApESPD();
                initializeWithCommonFields(espd, organization, dto);
                rAccessPoints.save(espd);
                return new AccessPointDetailInOrganizationDTO(espd);
            case "RSMO":
                final ApRSMO rsmo = new ApRSMO();
                initializeWithCommonFields(rsmo, organization, dto);
                rAccessPoints.save(rsmo);
                return new AccessPointDetailInOrganizationDTO(rsmo);
            case "ZSPD":
                final ApZSPD zspd = new ApZSPD();
                initializeWithCommonFields(zspd, organization, dto);
                rAccessPoints.save(zspd);
                return new AccessPointDetailInOrganizationDTO(zspd);
            case "CONTRACT":
                final ApContract contract = new ApContract();
                initializeWithCommonFields(contract, organization, dto);
                rAccessPoints.save(contract);
                return new AccessPointDetailInOrganizationDTO(contract);
            default:
                throw new Exception("Wrong Access Point !!!TYPE!!!");
        }
    }

    private AccessPoint initializeWithCommonFields(final AccessPoint item, @NotNull final Organization organization, AccessPointNewDTO dto) {
        item.setAddress(dto.getAddress());
        item.setBillingId(dto.getBilling_id());
        item.setContractor(dto.getContractor());
        item.setCustomer(dto.getCustomer());
        item.setDeclaredSpeed(dto.getDeclaredSpeed());
        item.setDescription(dto.getDescription());
        item.setIpConfig(dto.getIp_config());
//                item.setMaxAmount(dto.get);
//                item.setNetTrafficLastMonth(dto.get);
//                item.setNetTrafficLastWeek(dto.get);
        item.setNode(dto.getNode());
        dto.getPoint().setSRID(4326);
        item.setPoint(dto.getPoint());
        item.setQuality(dto.getQuality());
        item.setState(dto.getState());
        item.setOrganization(organization);
//                item.setUcn(dto.getUcn());
        item.setVisible(dto.getVisible());
//                item.setGovernmentDevelopmentProgram( dto.getGovernment_program());
//                item.setInternetAccess(r dto.getInternetAccess());
//                item.setCompleted(dto.getBilling_id());
//                item.setOperator(dto.getOperator());
        item.setDeleted(Boolean.FALSE);
        return item;
    }
}
