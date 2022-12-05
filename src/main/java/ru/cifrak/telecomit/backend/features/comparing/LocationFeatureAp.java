package ru.cifrak.telecomit.backend.features.comparing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "access_point")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationFeatureAp {

    @Id
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "point")
    private Point point;

    @Column(name = "fun_customer")
    private String funCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_internet_access")
    private TypeInternetAccess internetAccess;

    private String declaredSpeed;

    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "contract")
    private String contract;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "change")
    private String change;

    @Column(name = "date_connection_or_change")
    private LocalDate dateConnectionOrChange;

    @Column(name = "num_incoming_message")
    private String numIncomingMessage;

    @Column(name = "commentary")
    private String commentary;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "visible")
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_organization")
    private Organization organization;

    @Column(name = "espd_white_ip")
    private String espdWhiteIp;

    @Column(name = "num_source_emails_RTK")
    private String numSourceEmailsRTK;

    @Column(name = "one_time_pay")
    private BigDecimal oneTimePay;

    @Column(name = "monthly_pay")
    private BigDecimal monthlyPay;

    @Column(name = "zspd_white_ip")
    private String zspdWhiteIp;

    @Column(name = "avail_zspd_or_method_con_to_zspd")
    private String availZspdOrMethodConToZspd;

    @Column(name = "date_commissioning")
    private LocalDate dateCommissioning;

    public LocationFeatureAp(AccessPoint someAp) {
        this.id = someAp.getId();
        this.address = someAp.getAddress();
        this.point = someAp.getPoint();
        this.funCustomer = someAp.getFunCustomer();
        this.internetAccess = someAp.getInternetAccess();
        this.declaredSpeed = someAp.getDeclaredSpeed();
        this.contractId = someAp.getContractId();
        this.contract = someAp.getContract();
        this.contacts = someAp.getContacts();
        this.change = someAp.getChange();
        this.dateConnectionOrChange = someAp.getDateConnectionOrChange();
        this.numIncomingMessage = someAp.getNumIncomingMessage();
        this.commentary = someAp.getCommentary();
        this.deleted = someAp.getDeleted();
        this.visible = someAp.getVisible();
        this.organization = someAp.getOrganization();

        if (someAp instanceof ApESPD) {
            this.espdWhiteIp = ((ApESPD) someAp).getEspdWhiteIp();
            this.numSourceEmailsRTK = ((ApESPD) someAp).getNumSourceEmailsRTK();
            this.oneTimePay = ((ApESPD) someAp).getOneTimePay();
            this.monthlyPay = ((ApESPD) someAp).getMonthlyPay();
            this.zspdWhiteIp = ((ApESPD) someAp).getZspdWhiteIp();
            this.availZspdOrMethodConToZspd = ((ApESPD) someAp).getAvailZspdOrMethodConToZspd();
        } else if (someAp instanceof ApSMO) {
            this.dateCommissioning = ((ApSMO) someAp).getDateCommissioning();
        }
    }

    @JsonIgnore
    public LocationFeatureAp cloneWithNullId() {
        return new LocationFeatureAp(
                null,
                getAddress(), getPoint(), getFunCustomer(),getInternetAccess(), getDeclaredSpeed(), getContractId(),
                getContract(), getContacts(), getChange(), getDateConnectionOrChange(), getNumIncomingMessage(),
                getCommentary(), getDeleted(), getVisible(), getOrganization(),
                getEspdWhiteIp(), getNumSourceEmailsRTK(), getOneTimePay(), getMonthlyPay(),
                getZspdWhiteIp(), getAvailZspdOrMethodConToZspd(),
                getDateCommissioning()
        );
    }

    @JsonIgnore
    public AccessPoint convertToAccessPoint() {
        AccessPoint result = new AccessPoint();

        if (this.getDateCommissioning() != null) {
            result = new ApSMO();
        } else if (this.getEspdWhiteIp() != null) {
            result = new ApESPD();
        }

        result.setId(this.getId());
        result.setAddress(this.getAddress());
        result.setPoint(this.getPoint());
        result.setFunCustomer(this.getFunCustomer());
        result.setInternetAccess(this.getInternetAccess());
        result.setDeclaredSpeed(this.getDeclaredSpeed());
        result.setContractId(this.getContractId());
        result.setContract(this.getContract());
        result.setContacts(this.getContacts());
        result.setChange(this.getChange());
        result.setDateConnectionOrChange(this.getDateConnectionOrChange());
        result.setNumIncomingMessage(this.getNumIncomingMessage());
        result.setCommentary(this.getCommentary());
        result.setDeleted(this.getDeleted());
        result.setVisible(this.getVisible());
        result.setOrganization(this.getOrganization());

        if (this.getDateCommissioning() != null) {
            ((ApSMO) result).setDateCommissioning(this.getDateCommissioning());
        } else if (this.getEspdWhiteIp() != null) {
            ((ApESPD) result).setEspdWhiteIp(this.getEspdWhiteIp());
            ((ApESPD) result).setNumSourceEmailsRTK(this.getNumSourceEmailsRTK());
            ((ApESPD) result).setOneTimePay(this.getOneTimePay());
            ((ApESPD) result).setMonthlyPay(this.getMonthlyPay());
            ((ApESPD) result).setZspdWhiteIp(this.getZspdWhiteIp());
            ((ApESPD) result).setAvailZspdOrMethodConToZspd(this.getAvailZspdOrMethodConToZspd());
        }

        return result;
    }

}