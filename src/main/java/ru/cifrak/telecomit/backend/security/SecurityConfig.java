package ru.cifrak.telecomit.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @org.springframework.context.annotation.Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder()  {
        return new BCryptPasswordEncoder();
//        return new Pbkdf2PasswordEncoder();
    }

    @org.springframework.core.annotation.Order(1)
    @Configuration
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        TokenUserDetailsService tokenUserDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .addFilter(requestHeaderAuthenticationFilter())
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth", "/api/exchange_temp_token").permitAll()
                .antMatchers("/api/esia-auth/**").permitAll()
//                .antMatchers("/api/**").hasAuthority()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
            ;
        }

        public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader(HttpHeaders.AUTHORIZATION);
            requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
            requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);

            return requestHeaderAuthenticationFilter;
        }

        public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
            preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(tokenUserDetailsService/*userDetailsService()*/));

            return preAuthenticatedAuthenticationProvider;
        }

    }
    @org.springframework.core.annotation.Order(2)
    @Configuration
    public static class AuthWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        TokenUserDetailsService tokenUserDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/auth/**")
                .addFilter(requestHeaderAuthenticationFilter())
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/login/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
            ;
        }

        public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader(HttpHeaders.AUTHORIZATION);
            requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
            requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);

            return requestHeaderAuthenticationFilter;
        }

        public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
            preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(tokenUserDetailsService/*userDetailsService()*/));

            return preAuthenticatedAuthenticationProvider;
        }

    }

    @org.springframework.core.annotation.Order(3)
    @Configuration
    public static class AdminWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        UsernameUserDetailsService usernameUserDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/admin/**")
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().hasRole("ADMIN")//.authenticated()

                .and()
                .formLogin()
                .loginPage("/admin/loginPage")
                .loginProcessingUrl("/admin/login")
                .failureUrl("/admin/loginPage?error=loginError")
                .failureForwardUrl("/admin/loginPage?error=loginError")
                .defaultSuccessUrl("/admin/")

                .and()
                .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/loginPage")
                .deleteCookies("JSESSIONID")
                .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(usernameUserDetailsService);
        }
    }

//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("password")
//                .roles("USER");
//    }
//
//    protected void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .anyRequest().authenticated()
////                .and().httpBasic();
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .antMatchers(HttpMethod.HEAD, "/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/**").permitAll()
//                .antMatchers(HttpMethod.PUT, "/**").permitAll()
//                .antMatchers(HttpMethod.PATCH, "/**").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.TRACE, "/**").permitAll()
//        ;
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers(HttpMethod.GET, "/**")
//                .antMatchers(HttpMethod.HEAD, "/**")
//                .antMatchers(HttpMethod.POST, "/**")
//                .antMatchers(HttpMethod.PUT, "/**")
//                .antMatchers(HttpMethod.PATCH, "/**")
//                .antMatchers(HttpMethod.DELETE, "/**")
//                .antMatchers(HttpMethod.OPTIONS, "/**")
//                .antMatchers(HttpMethod.TRACE, "/**")
//        ;
//    }
}