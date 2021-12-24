package ru.cifrak.telecomit.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.repository.AccessPointDAO;

import java.util.List;

@RestController
public class ApiCompleatAddressImpl implements ApiCompleatAddress {
    private final AccessPointDAO accessPointDAO;

    @Autowired
    public ApiCompleatAddressImpl(AccessPointDAO accessPointDAO) {
        this.accessPointDAO = accessPointDAO;
    }

    @Override
    public List<String> getVariantsForCompleat(String address) {
        return accessPointDAO.findAllAddressesByOccurrence(address);
    }
}
