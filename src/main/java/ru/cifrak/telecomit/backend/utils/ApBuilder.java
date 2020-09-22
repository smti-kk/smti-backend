/*
package ru.cifrak.telecomit.backend.utils;

import ru.cifrak.telecomit.backend.api.dto.AccessPointNewDTO;
import ru.cifrak.telecomit.backend.entities.ApSMO;
import ru.cifrak.telecomit.backend.entities.Organization;

public class ApBuilder {
    public static ApBuilder build() {
        return new ApBuilder();
    }

    public ApSMO convert(Organization organization, AccessPointNewDTO item) {
        ApSMO e = new ApSMO();
        e.setOrganization(organization);
        e.setAddress (item.getAddress());
        e.setBillingId (item.getBilling_id());
        e.setIn (item.getInternetAccess());
        e.set (item.getContractor());
        e.set (item.getCustomer());
        e.set (item.getDescription());
        e.set (item.getGovernment_program());
        e.set (item.getIp_config());
        e.set (item.getMax_amount());
        e.set (item.getName());
        e.set (item.getNode());
        e.set (item.getOperator());
        e.set (item.getQuality());
        e.set (item.getState());
        e.set (item.getUcn());
        e.set (item.getVisible());
        e.set (item.getPoint());
    }
}
*/
