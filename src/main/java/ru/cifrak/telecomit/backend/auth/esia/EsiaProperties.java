package ru.cifrak.telecomit.backend.auth.esia;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Esia properties class
 * clientId: client system id at ESIA
 * keysDir:
 * certificateFileName: path to client system certificate file
 * privateKeyFileName: path to client system private key
 * tokenCheckKeyFileName: path to ESIA key to verify access token with
 * redirectUrl: uri, where browser will be redirected after authorization
 * serviceUrl: url of ESIA service
 * scopes: scopes keywords
 */

@Data
@ConfigurationProperties(prefix = "esia")
public class EsiaProperties {
    private String clientId;
    private String keysDir;
    private String certificate;
    private String privateKey;
    private String tokenCheckKey;
    private String redirectUrl;
    private String redirectAfterLoginUrl;
    private String serviceUrl;
    private String scopes;
}
