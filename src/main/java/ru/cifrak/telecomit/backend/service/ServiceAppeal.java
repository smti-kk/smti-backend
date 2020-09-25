package ru.cifrak.telecomit.backend.service;

import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.List;

public interface ServiceAppeal {
    List<Appeal> findAll();
    Appeal getOne(Integer id) throws NotFoundException;
    void delete(Integer id);
    Appeal updateOrCreate(Appeal appeal);
}
