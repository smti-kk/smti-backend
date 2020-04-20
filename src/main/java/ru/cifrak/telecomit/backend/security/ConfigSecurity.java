/*
package ru.cifrak.telecomit.backend.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/**")
                .antMatchers(HttpMethod.HEAD, "/**")
                .antMatchers(HttpMethod.POST, "/**")
                .antMatchers(HttpMethod.PUT, "/**")
                .antMatchers(HttpMethod.PATCH, "/**")
                .antMatchers(HttpMethod.DELETE, "/**")
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers(HttpMethod.TRACE, "/**")
        ;
    }
}
*/