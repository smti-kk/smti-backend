package ru.cifrak.telecomit.backend;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Config {

    @Bean
    public Module datatypeHibernateModule() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        return hibernate5Module;
    }
}
