package ru.cifrak.telecomit.backend.repository;

import java.util.List;

public interface AccessPointDAO {
    List<String> findAllAddressesByOccurrence(String stringOccurrence);
}
