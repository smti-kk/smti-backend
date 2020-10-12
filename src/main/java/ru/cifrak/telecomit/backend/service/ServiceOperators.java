package ru.cifrak.telecomit.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface ServiceOperators {
    Operator item(@NotNull Integer id) throws NotFoundException;

    List<Operator> findAll();

    Page<Operator> findAll(Pageable pageable);

    Map<String, List<Operator>> grouped();

    List<Operator> internet();

    List<Operator> mobile();

    List<Operator> postal();

    List<Operator> ats();

    List<Operator> radio();

    List<Operator> television();

    List<Operator> payphone();

    List<Operator> infomat();

    Operator save(Operator operator);

    String createIcon(MultipartFile icon);

    void delete(Integer id);
}
