package ru.cifrak.telecomit.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryAppeal;

import java.util.List;

@Service
public class ServiceAppealImpl implements ServiceAppeal {
    private final RepositoryAppeal repositoryAppeal;

    public ServiceAppealImpl(RepositoryAppeal repositoryAppeal) {
        this.repositoryAppeal = repositoryAppeal;
    }

    @Override
    public List<Appeal> findAll() {
        return repositoryAppeal.findAll();
    }

    @Override
    public Appeal getOne(Integer id) throws NotFoundException {
        return repositoryAppeal
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        repositoryAppeal.deleteById(id);
    }

    @Override
    @Transactional
    public Appeal updateOrCreate(Appeal appeal) {
        return repositoryAppeal.save(appeal);
    }
}
