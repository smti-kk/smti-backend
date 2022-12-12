package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.ChangeSource;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditAction;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeatureAp;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableAP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class ApesSaveService {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryAccessPoints repositoryAccessPoints;

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

//    private final RepositorySmoType repositorySmoType;
//
//    private final RepositoryOrganizationType repositoryOrganizationType;
//
//    private final RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    private final RepositoryFeatureEdits repositoryFeatureEdits;

    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;

    private final ServiceWritableAP serviceWritableAP;

    private final LocationService locationService;

    public ApesSaveService(
            RepositoryLocation repositoryLocation,
            RepositoryAccessPoints repositoryAccessPoints,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
//            RepositorySmoType repositorySmoType,
//            RepositoryOrganizationType repositoryOrganizationType,
//            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram,
            RepositoryFeatureEdits repositoryFeatureEdits,
            RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
            ServiceWritableAP serviceWritableAP,
            LocationService locationService) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryAccessPoints = repositoryAccessPoints;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
//        this.repositorySmoType = repositorySmoType;
//        this.repositoryOrganizationType = repositoryOrganizationType;
//        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
        this.repositoryFeatureEdits = repositoryFeatureEdits;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableAP = serviceWritableAP;
        this.locationService = locationService;
    }

    public void save(List<? extends ApFromExcelDTO> apesDTO, String apType, User user) {
        for (ApFromExcelDTO apDTO : apesDTO) {
            List<AccessPoint> apes = repositoryAccessPoints.findByPointAndOrganization(
                    createPoint(apDTO.getLongitude(), apDTO.getLatitude()),
                    getOrganization(apDTO));
            AccessPoint accessPoint = new AccessPoint();
            FeatureEdit featureEdit;
            // TODO: переписать parseExcelDtoToEntity: AccessPoint заменить на LFAP
            // TODO: добавить репозиторий для LocationFeatureAp и работать с ним вместо repositoryAP
            if (apes.size() > 0) {
                accessPoint = apes.stream().max(Comparator.comparing(AccessPoint::getId)).get();
                LocationFeatureAp locationFeatureAp = new LocationFeatureAp(accessPoint);

                if (apDTO.getActivity().equalsIgnoreCase("нет")) {
                    this.parseExcelDtoToEntity(apDTO, accessPoint, apType);
                    featureEdit = new FeatureEdit(locationFeatureAp, FeatureEditAction.DELETE);
                } else {
                    LocationFeatureAp clonedLFAP = locationFeatureAp.cloneWithNullId();
                    AccessPoint clonedAp = clonedLFAP.convertToAccessPoint(repositoryAccessPoints);
                    this.parseExcelDtoToEntity(apDTO, clonedAp, apType);
                    clonedAp = repositoryAccessPoints.save(clonedAp);
                    clonedLFAP = new LocationFeatureAp(clonedAp);
                    featureEdit = new FeatureEdit(locationFeatureAp, clonedLFAP);
                }
            } else {
                if (apDTO.getActivity().equalsIgnoreCase("нет")) {
                    continue;
                }
                switch (TypeAccessPoint.valueOf(apType)) {
                    case ESPD:
                        accessPoint = new ApESPD();
                        break;
                    case SMO:
                        accessPoint = new ApSMO();
                        break;
                }
                this.parseExcelDtoToEntity(apDTO, accessPoint, apType);
                accessPoint = repositoryAccessPoints.save(accessPoint);
                LocationFeatureAp locationFeatureAp = new LocationFeatureAp(accessPoint);
                featureEdit = new FeatureEdit(locationFeatureAp, FeatureEditAction.CREATE);
            }

            repositoryFeatureEdits.save(featureEdit);
            LocationFeaturesEditingRequest importRequest = new LocationFeaturesEditingRequest(
                    accessPoint.getOrganization().getLocation().getId(),
                    "",
                    user,
                    ChangeSource.IMPORT,
                    Collections.singleton(featureEdit)
            );
            repositoryLocationFeaturesRequests.save(importRequest);
            importRequest.accept(serviceWritableAP);
            locationService.refreshCache();
        }
    }

    private void parseExcelDtoToEntity(ApFromExcelDTO apDTO, AccessPoint accessPoint, String apType) {

        switch (TypeAccessPoint.valueOf(apType)) {
            case ESPD:
                try {
                    Method method = ApESPD.class.getMethod("setEspdWhiteIp", String.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint,
                            ((ApESPDFromExcelDTO) apDTO).getEspdWhiteIp());
                    method.setAccessible(false);

                    method = ApESPD.class.getMethod("setNumSourceEmailsRTK", String.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint,
                            ((ApESPDFromExcelDTO) apDTO).getNppSourceMessageRTK());
                    method.setAccessible(false);

                    method = ApESPD.class.getMethod("setOneTimePay", BigDecimal.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint,
                            BigDecimal.valueOf(
                                    Double.parseDouble(
                                            ((ApESPDFromExcelDTO) apDTO).getOneTimePay()
                                    )
                            ));
                    method.setAccessible(false);

                    method = ApESPD.class.getMethod("setMonthlyPay", BigDecimal.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint,
                            BigDecimal.valueOf(
                                    Double.parseDouble(
                                            ((ApESPDFromExcelDTO) apDTO).getMonthlyPay()
                                    )
                            ));
                    method.setAccessible(false);

                    method = ApESPD.class.getMethod("setZspdWhiteIp", String.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint, ((ApESPDFromExcelDTO) apDTO).getZspdWhiteIp());
                    method.setAccessible(false);

                    method = ApESPD.class.getMethod("setAvailZspdOrMethodConToZspd", String.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint, ((ApESPDFromExcelDTO) apDTO).getAvailZSPD());
                    method.setAccessible(false);
                } catch (NoSuchMethodException e) {
                    log.error("Не опеределен метод " + e.getMessage() + "!");
                } catch (InvocationTargetException e) {
                    log.error("Вызванный метод выдал исключение " + e.getMessage() + "!");
                } catch (IllegalAccessException e) {
                    log.error("Вызванный метод не имеет доступа" + e.getMessage() + "!");
                }
                break;

            case SMO:
                try {
                    Method method = ApSMO.class.getMethod("setDateCommissioning", LocalDate.class);
                    method.setAccessible(true);
                    method.invoke(accessPoint,
                            LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(
                                            DateUtil.getJavaDate(
                                                    Double.parseDouble(
                                                            ((ApSMOFromExcelDTO) apDTO)
                                                                    .getCommissioningDate()
                                                    )
                                            ).getTime()
                                    ),
                                    ZoneId.systemDefault()
                            ).toLocalDate()
                    );
                } catch (NoSuchMethodException e) {
                    log.error("Не опеределен метод " + e.getMessage() + "!");
                } catch (InvocationTargetException e) {
                    log.error("Вызванный метод выдал исключение " + e.getMessage() + "!");
                } catch (IllegalAccessException e) {
                    log.error("Вызванный метод не имеет доступа" + e.getMessage() + "!");
                }
                break;
        }

//                    accessPoint.setGovernmentDevelopmentProgram(repositoryGovernmentDevelopmentProgram.findByAcronym(apDTO.getProgram()));
        accessPoint.setOrganization(this.getOrganization(apDTO));
        accessPoint.setFunCustomer(apDTO.getFunctionalCustomer());
        accessPoint.getOrganization().setFunCustomer(apDTO.getFunctionalCustomer());
        accessPoint.setAddress(apDTO.getAddress());
        accessPoint.setPoint(this.createPoint(apDTO.getLongitude(), apDTO.getLatitude()));
        accessPoint.setInternetAccess(
                repositoryInternetAccessType.findByName(apDTO.getTypeInternetAccess())
        );
        accessPoint.setDeclaredSpeed(apDTO.getDeclaredSpeed());
        accessPoint.setContractId(Integer.parseInt(apDTO.getContractId()));
        accessPoint.setContract(apDTO.getContract());
        accessPoint.setContacts(apDTO.getContacts());
        accessPoint.setChange(apDTO.getChangeType());
        accessPoint.setDateConnectionOrChange(
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(
                                DateUtil.getJavaDate(
                                        Double.parseDouble(
                                                apDTO.getConnectionOrChangeDate()
                                        )
                                ).getTime()
                        ),
                        ZoneId.systemDefault()
                ).toLocalDate()
        );
        accessPoint.setNumIncomingMessage(apDTO.getNppIncomingMessage());
        accessPoint.setCommentary(apDTO.getComments());
        accessPoint.setVisible(!apDTO.getActivity().equalsIgnoreCase("нет"));
        accessPoint.setDeleted(apDTO.getActivity().equalsIgnoreCase("нет"));
    }

    private Organization getOrganization(ApFromExcelDTO ap) {
        Organization organization = repositoryOrganization.findByFias(UUID.fromString(ap.getFias()));
        if (organization == null) {
            organization = new Organization();
            organization.setFias(UUID.fromString(ap.getFias()));
            Location location = repositoryLocation.findByFias(UUID.fromString(ap.getFiasLocation()));
            if (location != null) {
                organization.setLocation(location);
            }
            organization.setName(ap.getName());
            organization.setAddress(ap.getAddress());
            organization.setFunCustomer(ap.getFunctionalCustomer());
//            organization.setSmo(repositorySmoType.findByName(ap.getSmo()));
//            organization.setType(repositoryOrganizationType.findByName(ap.getType()));
            organization.setAcronym("");
            organization.setInn("");
            organization.setKpp("");
            repositoryOrganization.save(organization);
        }
        return organization;
    }

    private Point createPoint(String x, String y) {
        Point p = new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(x), Double.parseDouble(y)));
        p.setSRID(4326);
        return p;
    }
}
