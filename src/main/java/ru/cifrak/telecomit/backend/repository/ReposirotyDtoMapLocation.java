package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.cifrak.telecomit.backend.api.dto.DtoMapLocation;

import java.util.List;

public interface ReposirotyDtoMapLocation extends PagingAndSortingRepository<DtoMapLocation, Integer> {
    List<DtoMapLocation> findAll();
}
