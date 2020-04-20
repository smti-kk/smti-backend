package ru.cifrak.telecomit.backend.auth.esia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Esia authentication connector
 */

public class EsiaAuth {
    protected final String _ESIA_ISSUER_NAME = "http://esia.gosuslugi.ru/";
    protected final String _AUTHORIZATION_URL = "/aas/oauth2/ac";
    protected final String _TOKEN_EXCHANGE_URL = "/aas/oauth2/te";
    protected final String _LOGOUT_URL = "idp/ext/Logout";

    private final EsiaProperties properties;

    /**
     * :param EsiaSettings settings: connector settings
     * @param properties
     */
    public EsiaAuth(EsiaProperties properties) {
        this.properties = properties;
    }

    /**
     * Return url which end-user should visit to authorize at ESIA.
     * @param state identifier, will be returned as GET parameter in redirected request after auth..
     * @param redirect_params get parameters in redirect url
     * @return url which end-user should visit to authorize at ESIA
     */
    public String get_auth_url(String state, Map<String, String> redirect_params) throws CertificateException,
            NoSuchAlgorithmException, IOException, CMSException, OperatorCreationException,
            NoSuchProviderException, InvalidKeySpecException {
        // TODO: add test for redirect_params parameter

        final String redirect_uri;
        if (redirect_params != null) {
            redirect_uri = String.format("%s?%s", properties.getRedirectUrl(), EsiaUtils.urlencode(redirect_params));
        } else {
            redirect_uri = properties.getRedirectUrl();
        }

        Map<String, String> params = new TreeMap<String, String>() {{
            put("client_id", properties.getClientId());
            put("client_secret", "");
            put("redirect_uri", redirect_uri);
            put("scope", properties.getScopes());
            put("response_type", "code");
            put("state", state == null ? UUID.randomUUID().toString() : state);
            put("timestamp", EsiaUtils.get_timestamp());
            put("access_type", "offline");
        }};

        params = EsiaUtils.sign_params(params,
                String.format("%s/%s", properties.getKeysDir(), properties.getCertificate()),
                String.format("%s/%s", properties.getKeysDir(), properties.getPrivateKey())
        );

        return String.format("%s%s?%s", properties.getServiceUrl(), _AUTHORIZATION_URL, EsiaUtils.urlencode(params));
    }

    public String get_logout_url(Map<String, String> redirect_params) {
        final String redirect_uri;
        if (redirect_params != null) {
            redirect_uri = String.format("%s?%s", properties.getRedirectUrl(), EsiaUtils.urlencode(redirect_params));
        } else {
            redirect_uri = properties.getRedirectUrl();
        }

        Map<String, String> params = new TreeMap<String, String>() {{
            put("client_id", properties.getClientId());
            put("redirect_uri", redirect_uri);
        }};

        return String.format("%s%s?%s", properties.getServiceUrl(), _LOGOUT_URL, EsiaUtils.urlencode(params));
    }

    public EsiaInformationConnector complete_authorization(String code, String state) throws CertificateException,
            InvalidKeySpecException, NoSuchAlgorithmException, IOException, OperatorCreationException,
            NoSuchProviderException, CMSException {
        return complete_authorization(code, state, true);
    }

    /**
     * Exchanges received code and state to access token, validates token (optionally), extracts ESIA user id from
     * token and returns ESIAInformationConnector instance.
     *
     * @param code
     * @param state
     * @param validate_token perform token validation
     * @return EsiaInformationConnector
     * :raises IncorrectJsonError: if response contains invalid json body
     * :raises HttpError: if response status code is not 2XX
     * :raises IncorrectMarkerError: if validate_token set to True and received token cannot be validated
     */
    public EsiaInformationConnector complete_authorization(String code, String state, boolean validate_token)
            throws CertificateException, NoSuchAlgorithmException, IOException, CMSException, OperatorCreationException,
            NoSuchProviderException, InvalidKeySpecException {

        Map<String, String> params = new TreeMap<String, String>() {{
            put("client_id", properties.getClientId());
            put("code", code);
            put("grant_type", "authorization_code");
            put("redirect_uri", properties.getRedirectUrl());
            put("timestamp", EsiaUtils.get_timestamp());
            put("token_type", "Bearer");
            put("scope", properties.getScopes());
            put("state", state);
        }};

        params = EsiaUtils.sign_params(params,
                String.format("%s/%s", properties.getKeysDir(), properties.getCertificate()),
                String.format("%s/%s", properties.getKeysDir(), properties.getPrivateKey())
        );

        final String url = String.format("%s%s", properties.getServiceUrl(), _TOKEN_EXCHANGE_URL);

        final Map<String, Object> response_json = EsiaUtils.make_request(url, "POST", null, params);

        final String id_token = (String) response_json.get("id_token");

        Jwt payload = _parse_token(id_token);
        // TODO validate token

        return new EsiaInformationConnector((String) response_json.get("access_token"), _get_user_id(payload), properties);
    }

    protected Jwt _parse_token(String token) {
        return JwtHelper.decode(token);
    }

    protected Long _get_user_id(Jwt payload) throws JsonProcessingException {
        final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> json_val = mapper.readValue(payload.getClaims(), typeRef);
        return Optional.ofNullable((Map<String, Object>) json_val.get("urn:esia:sbj"))
                .map(m -> (Number) m.get("urn:esia:sbj:oid"))
                .map(Number::longValue)
                .orElse(null);
    }
}
