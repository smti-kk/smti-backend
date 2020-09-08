package ru.cifrak.telecomit.backend.api.dto.external;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Value;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.security.UTM5Config;

import java.util.Map;

@Value
public class ExtUtmDtoRequestCreateUser {
    String sessionId;
    String login;
    String password;
    String full_name;
    Boolean is_juridical = Boolean.FALSE;
    String juridical_address = "";
    String actual_addres = "";
    String flat_numbe = "";
    String entranc = "";
    String floo = "";
    String distric = "";
    String buildin = "";
    String passpor = "";
    Long house_id = 0l;
    String work_telephon = "";
    String home_telephon = "";
    String mobile_telephon = "";
    String web_pag = "";
    String icq_numbe = "";
    String tax_numbe = "";
    String kpp_numbe = "";
    String emai = "";
    Long bank_id = 0l;
    String bank_accoun = "";
    String comment = "";
    String personal_manage = "";
    Long connect_date = 0l;
    Boolean is_send_invoice = Boolean.FALSE;
    Long advance_payment = 0l;
    Long router_id = 0l;
    Long port_number = 0l;
    Long binded_currency_id = 0l;
    String additional_params = null;
//    ArrayNode jsonParamsAdditionalArray = jsonMapper.createArrayNode();
//        jsonParams.set("additional_params",jsonParamsAdditionalArray);
//    ArrayNode jsonParamsGroupsArray = jsonMapper.createArrayNode();
//        jsonParams.set("groups",jsonParamsGroupsArray);
    String [] groups = null;
    Boolean is_blocked = Boolean.FALSE;
    Long balance = 0l;
    Long credit = 0l;
    Long vat_rate = 0l;
    Long sale_tax_rate = 0l;
    Long int_status = 0l;

    public ExtUtmDtoRequestCreateUser(AccessPoint ap, UTM5Config utm5Config, String sessionId) {
        if (ap.getOrganization().getAcronym() != null && !ap.getOrganization().getAcronym().isEmpty()) {
            this.full_name = ap.getOrganization().getAcronym() + " (" + ap.getAddress() + ")";
        } else {
            this.full_name = ap.getOrganization().getId() + " (" + ap.getAddress() + ")";
        }
        this.login ="telecomit-"+ap.getId();
        this.password = utm5Config.getUserpwd();
        this.sessionId = sessionId;
    }
}
