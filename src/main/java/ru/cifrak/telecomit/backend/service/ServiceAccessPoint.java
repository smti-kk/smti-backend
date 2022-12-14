package ru.cifrak.telecomit.backend.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.AccessPointDetailInOrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.AccessPointNewDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.ChangeSource;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditAction;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeatureAp;
import ru.cifrak.telecomit.backend.repository.*;

import java.util.Collections;

@Transactional
@Service
public class ServiceAccessPoint {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryLocation rLocation;
    private final RepositorySmoType rTypeSmo;
    private final RepositoryOrganizationType rTypeOrganization;
    private final RepositoryGovernmentProgram rGovernmentProgram;
    private final RepositoryInternetAccessType rInternetAccessType;

    private final RepositoryFeatureEdits repositoryFeatureEdits;

    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;

    private final RepositoryChanges rChanges;

    private final ServiceWritableAP serviceWritableAP;

    private final LocationService locationService;


    public ServiceAccessPoint(RepositoryOrganization rOrganization,
                              RepositoryAccessPoints rAccessPoints,
                              RepositoryLocation rLocation,
                              RepositorySmoType rTypeSmo,
                              RepositoryOrganizationType rTypeOrganization,
                              RepositoryGovernmentProgram rGovernmentProgram,
                              RepositoryInternetAccessType rInternetAccessType,
                              RepositoryFeatureEdits repositoryFeatureEdits,
                              RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                              RepositoryChanges repositoryChanges,
                              ServiceWritableAP serviceWritableAP,
                              LocationService locationService) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rLocation = rLocation;
        this.rTypeSmo = rTypeSmo;
        this.rTypeOrganization = rTypeOrganization;
        this.rGovernmentProgram = rGovernmentProgram;
        this.rInternetAccessType = rInternetAccessType;
        this.repositoryFeatureEdits = repositoryFeatureEdits;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.rChanges = repositoryChanges;
        this.serviceWritableAP = serviceWritableAP;
        this.locationService = locationService;
    }

    public AccessPointDetailInOrganizationDTO giveNewCreatedAccessPoint(@NotNull final Organization organization,
                                                                        final AccessPointNewDTO dto,
                                                                        User user) throws Exception {
        AccessPoint accessPoint;
        AccessPoint returnValue;
        switch (dto.getType()) {
            case "SMO":
                accessPoint = new ApSMO(dto.getDateCommissioning());
                break;
            case "ESPD":
                accessPoint = new ApESPD(dto.getEspdWhiteIp(),
                                         dto.getNumSourceEmailsRTK(),
                                         dto.getOneTimePay(),
                                         dto.getMounthlyPay(),
                                         dto.getZspdWhiteIp(),
                                         dto.getAvailZspdOrMethodConToZspd());
                break;
//            case "RSMO":
//                final ApRSMO rsmo = new ApRSMO();
//                initializeWithCommonFields(rsmo, organization, dto);
//                rAccessPoints.save(rsmo);
//                return new AccessPointDetailInOrganizationDTO(rsmo);
//            case "EMSPD":
//                final ApEMSPD emspd = new ApEMSPD();
//                initializeWithCommonFields(emspd, organization, dto);
//                //TODO: here we should insert real data
//                emspd.setHardware(null);
//                //TODO: here we should insert real data
//                emspd.setSoftware(null);
//                emspd.setEquipment(dto.getEquipment());
//                emspd.setSoftType(dto.getSoftType());
//                rAccessPoints.save(emspd);
//                return new AccessPointDetailInOrganizationDTO(emspd);
//            case "CONTRACT":
//                final ApContract contract = new ApContract();
//                initializeWithCommonFields(contract, organization, dto);
//                contract.setNumber(dto.getNumber());
//                contract.setAmount(dto.getAmount());
//                contract.setStarted(dto.getStarted());
//                contract.setEnded(dto.getEnded());
//                rAccessPoints.save(contract);
//                return new AccessPointDetailInOrganizationDTO(contract);
            default:
                throw new Exception("Wrong Access Point !!!TYPE!!!");
        }

        LocationFeatureAp locationFeatureAp;
        FeatureEdit featureEdit;
        if (dto.getId() == null || dto.getId() <= 0) {
            initializeWithCommonFields(accessPoint, organization, dto);
            locationFeatureAp = new LocationFeatureAp(accessPoint);
            featureEdit = new FeatureEdit(locationFeatureAp, FeatureEditAction.CREATE);
            returnValue = rAccessPoints.save(accessPoint);
        } else {
            accessPoint = rAccessPoints.getOne(dto.getId());
            locationFeatureAp = new LocationFeatureAp(accessPoint);
            LocationFeatureAp clonedLFAP = locationFeatureAp.cloneWithNullId();
            AccessPoint clonedAp = clonedLFAP.convertToAccessPoint(rAccessPoints);
            initializeWithCommonFields(clonedAp, organization, dto);
            clonedAp = rAccessPoints.saveAndFlush(clonedAp);
            clonedLFAP = new LocationFeatureAp(clonedAp);
            if (TypeAccessPoint.valueOf(dto.getType()).equals(TypeAccessPoint.ESPD)) {
                clonedLFAP.setEspdWhiteIp(dto.getEspdWhiteIp());
                clonedLFAP.setNumSourceEmailsRTK(dto.getNumSourceEmailsRTK());
                clonedLFAP.setOneTimePay(dto.getOneTimePay());
                clonedLFAP.setMonthlyPay(dto.getMounthlyPay());
                clonedLFAP.setZspdWhiteIp(dto.getZspdWhiteIp());
                clonedLFAP.setAvailZspdOrMethodConToZspd(dto.getAvailZspdOrMethodConToZspd());
            } else if (TypeAccessPoint.valueOf(dto.getType()).equals(TypeAccessPoint.SMO)) {
                clonedLFAP.setDateCommissioning(dto.getDateCommissioning());
            }
            returnValue = clonedLFAP.convertToAccessPoint(rAccessPoints);
            returnValue = rAccessPoints.saveAndFlush(returnValue);
            clonedLFAP = new LocationFeatureAp(returnValue);
            featureEdit = new FeatureEdit(locationFeatureAp, clonedLFAP);
        }

        repositoryFeatureEdits.saveAndFlush(featureEdit);
        LocationFeaturesEditingRequest editingRequest = new LocationFeaturesEditingRequest(
                accessPoint.getOrganization().getLocation().getId(),
                "",
                user,
                ChangeSource.EDITING,
                Collections.singleton(featureEdit)
        );
        repositoryLocationFeaturesRequests.saveAndFlush(editingRequest);
        editingRequest.accept(serviceWritableAP);
        locationService.refreshCache();

        return new AccessPointDetailInOrganizationDTO(returnValue);
    }

    private AccessPoint initializeWithCommonFields(final AccessPoint item, @NotNull final Organization organization, AccessPointNewDTO dto) {
        item.setAddress(dto.getAddress());
        item.setBillingId(dto.getBilling_id());
//        item.setContractor(dto.getContractor());
//        item.setCustomer(dto.getCustomer());
        item.setDeclaredSpeed(dto.getDeclaredSpeed());
        item.setDescription(dto.getDescription());
//        item.setIpConfig(dto.getIp_config());
//        item.setMaxAmount(dto.getMax_amount());
//        item.setNetworks(dto.getNode());
        dto.getPoint().setSRID(4326);
        item.setPoint(dto.getPoint());
        item.setQuality(dto.getQuality());
        item.setState(ParticipationStatus.NONE);
        item.setOrganization(organization);
        item.setUcn(dto.getUcn());
        item.setVisible(dto.getVisible());
//        item.setGovernmentDevelopmentProgram(dto.getGovernment_program() != null ? rGovernmentProgram.getOne(dto.getGovernment_program()) : null);
        item.setInternetAccess(dto.getInternetAccess() != null ? rInternetAccessType.getOne(dto.getInternetAccess()) : null);
        item.setCompleted(dto.getCompleted());
        item.setDeleted(Boolean.FALSE);
        item.setFunCustomer(organization.getFunCustomer() != null ? organization.getFunCustomer().getName() : null);
        item.setContractId(dto.getContractId());
        item.setContract(dto.getContract());
        item.setContacts(dto.getContacts());
        item.setChange(rChanges.getOne(dto.getChange()));
        item.setDateConnectionOrChange(dto.getDateConnectionOrChange());
        item.setNumIncomingMessage(dto.getNumIncomingMessage());
        item.setCommentary(dto.getCommentary());
        return item;
    }
}
