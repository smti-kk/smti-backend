package ru.cifrak.telecomit.backend.api.dto.external.utm5;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.security.UTM5Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtUtmDtoRequestCreateUser {
    String login;
    String password;
    String full_name;
    Boolean is_juridical = Boolean.FALSE;
    String juridical_address = "";
    String actual_address = "";
    String flat_number = "";
    String entrance = "";
    String floor = "";
    String district = "";
    String building = "";
    String passport = "";
    Long house_id = 0l;
    String work_telephone = "";
    String home_telephone = "";
    String mobile_telephone = "";
    String web_page = "";
    String icq_number = "";
    String tax_number = "";
    String kpp_number = "";
    String email = "";
    Long bank_id = 0l;
    String bank_account = "";
    String comments = "";
    String personal_manager = "";
    Long connect_date = 0l;
    Boolean is_send_invoice = Boolean.FALSE;
    Long advance_payment = 0l;
    Long router_id = 0l;
    Long port_number = 0l;
    Long binded_currency_id = 0l;
    List<String> additional_params = new ArrayList<>();
    List<String> groups = new ArrayList<>();
    Boolean is_blocked = Boolean.FALSE;
    Long balance = 0l;
    Long credit = 0l;
    Long vat_rate = 0l;
    Long sale_tax_rate = 0l;
    Long int_status = 0l;

    public ExtUtmDtoRequestCreateUser(AccessPoint ap, UTM5Config utm5Config, String sessionId) {
        this.full_name = ap.getOrganization().getId() + " (" + ap.getAddress() + ")";
        this.login = "telecomit-" + ap.getId();
        this.password = utm5Config.getUserpwd();
    }
}
