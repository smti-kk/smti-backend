package ru.cifrak.telecomit.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.*;

import java.util.List;

@Transactional
@Service
public class ServiceTechnicalCapabilities {
    private final RepositoryTcAts atss;
    private final RepositoryTcInternet internets;
    private final RepositoryTcMobile mobiles;
    private final RepositoryTcPost posts;
    private final RepositoryTcRadio radios;
    private final RepositoryTcTv tvs;
    private final RepositoryLocation locations;
    private final RepositoryOperator operators;

    public ServiceTechnicalCapabilities(RepositoryTcAts atss, RepositoryTcInternet internets, RepositoryTcMobile mobiles, RepositoryTcPost posts, RepositoryTcRadio radios, RepositoryTcTv tvs, RepositoryLocation locations, RepositoryOperator operators) {
        this.atss = atss;
        this.internets = internets;
        this.mobiles = mobiles;
        this.posts = posts;
        this.radios = radios;
        this.tvs = tvs;
        this.locations = locations;
        this.operators = operators;
    }

    public List<TcAts> allAts() {
        return atss.findAll();
    }

    public List<TcInternet> allInternet() {
        return internets.findAll();
    }

    public List<TcMobile> allMobile() {
        return mobiles.findAll();
    }

    public List<TcPost> allPost() {
        return posts.findAll();
    }

    public List<TcRadio> allRadio() {
        return radios.findAll();
    }

    public List<TcTv> allTv() {
        return tvs.findAll();
    }


    public Location getLoc() {
        return locations.getOne(11);
    }

    public Operator getOpe() {
        return operators.getOne(4);
    }

    public TcPost save(TcPost post) {
        return posts.save(post);
    }
}
