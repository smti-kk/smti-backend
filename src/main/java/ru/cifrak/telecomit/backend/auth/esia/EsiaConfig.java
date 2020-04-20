package ru.cifrak.telecomit.backend.auth.esia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsiaConfig {
    @Value("${esia.client-id}")
    private String clientId;

    @Value("${esia.redirect-url}")
    private String redirectUrl;

    @Value("${esia.keys-dir}")
    private String keysDir;

    @Value("${esia.certificate}")
    private String certificate;

    @Value("${esia.private-key}")
    private String privateKey;

    @Value("${esia.token-check-key}")
    private String tokenCheckKey;

    @Value("${esia.service-url}")
    private String serviceUrl;

    @Value("${esia.scopes}")
    private String scopes;

    @Value("${esia.redirect-after-login-url}")
    private String redirectAfterLoginUrl;


    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getScopes() {
        return scopes;
    }

    public String getKeysDir() {
        return keysDir;
    }

    public String getTokenCheckKey() {
        return tokenCheckKey;
    }

    public String getRedirectAfterLoginUrl() {
        return redirectAfterLoginUrl;
    }
}
