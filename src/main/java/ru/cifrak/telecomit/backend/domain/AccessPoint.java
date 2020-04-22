package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.cifrak.telecomit.backend.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public class AccessPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="ACCESS_POINT_ID_GENERATOR", sequenceName="SEQ_ACCESS_POINT")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCESS_POINT_ID_GENERATOR")
    @Column(unique=true, nullable=false)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date commissioning;

    private Integer completed;

    @Temporal(TemporalType.DATE)
    @Column(name="dismiss_date")
    private Date dismissDate;

    @Column(name="functional_customer", length=2147483647)
    private String functionalCustomer;

    @Column(nullable=false, length=6)
    private String quality;

    private Integer requests;

    @Column(length=4)
    private String state;

    @Column(name="technical_status", nullable=false, length=9)
    @JsonProperty("technical_status")
    private String technicalStatus;

    //bi-directional many-to-one association to CapabilitiesClarifypetition
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="clarify_petition_id")
    @JsonIgnore
    private CapabilitiesClarifypetition capabilitiesClarifypetition;

    //bi-directional many-to-one association to CatalogsContract
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contract_id")
    @JsonIgnore
    private CatalogsContract catalogsContract;

    //bi-directional many-to-one association to CatalogsGovernmentdevelopmentprogram
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="government_program_id")
    @JsonProperty("government_program")
    private CatalogsGovernmentDevelopmentProgram governmentProgram;

    //bi-directional many-to-one association to CatalogsLocation
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="location_id", nullable=false)
    @JsonIgnore
    private CatalogsLocation catalogsLocation;

    //bi-directional many-to-one association to CatalogsOperator
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="operator_id", nullable=false)
    @JsonProperty("operator")
    private CatalogsOperator catalogsOperator;

    @JsonProperty("active")
    public boolean isActive() {
        return technicalStatus.equals(TechnicalStatus.CONFIRMED.getName()) &&
                governmentProgram != null && completed.equals(DateUtils.getCurrentYear())
                || governmentProgram == null && commissioning == null
                || governmentProgram == null && commissioning.before(DateUtils.getCurrentDate());
    }

    @JsonProperty("archive")
    public boolean isArchive() {
        return technicalStatus.equals(TechnicalStatus.OUTDATED.getName());
    }

    @JsonProperty("planYear")
    public boolean isPlanYear() {
        return technicalStatus.equals(TechnicalStatus.CONFIRMED.getName()) &&
                governmentProgram != null && completed >= DateUtils.getCurrentYear()
                || governmentProgram == null && commissioning != null && commissioning.after(DateUtils.getCurrentDate());
    }

    @JsonProperty("planTwoYear")
    public boolean isPlanTwoYear() {
        return technicalStatus.equals(TechnicalStatus.CONFIRMED.getName()) &&
                governmentProgram != null && completed >= DateUtils.getCurrentYear()
                || governmentProgram == null && commissioning != null && DateUtils.getYear(commissioning) > DateUtils.getCurrentYear() + 2;
    }

    public AccessPoint() {
    }
}

/*
def get_planYear(self, instance: FTCInternet):
        """
        Mark current technical capabilities actual for planned year

        :param instance:
        :return:
        """
        if (instance.technical_status == TechnicalStatus.CONFIRMED) and (
                (instance.government_program is not None and datetime.now().year <= instance.completed)
                or (instance.government_program is None and instance.commissioning is not None and instance.commissioning > datetime.now().date())
        ):
            return True
        else:
            return False

    def get_planTwoYear(self, instance: FTCInternet):
        """
        Mark current technical capabilities actual for planned year

        :param instance:
        :return:
        """
        if (instance.technical_status == TechnicalStatus.CONFIRMED) and (
                (instance.government_program is None and instance.commissioning is not None and instance.commissioning.year > datetime.now().year+2)
        ):
            return True
        else:
            return False

def get_active(self, instance: FTCInternet):
        """
        Mark current technical capabilities actual for current year

        :param instance:
        :return:
        """
        if (instance.technical_status == TechnicalStatus.CONFIRMED) and (
                (instance.government_program is not None and instance.completed == datetime.now().year)
                or (instance.government_program is None and instance.commissioning is None)
                or (instance.government_program is None and instance.commissioning <= datetime.now().date())
        ):
            return True
        else:
            return False

    def get_archive(self, instance: FTCInternet):
        """
        NOW returning just ALL instances
        TODO: more accurate giveback by year.
        :param instance:
        :return:
        """
        if (instance.technical_status == TechnicalStatus.OUTDATED):
            return True
        else:
            return False

    class Meta:
        model = FTCInternet
        exclude = []
 */