package ru.cifrak.telecomit.backend.auth.esia;

import java.util.Map;
import java.util.TreeMap;

/**
 * Connector for fetching information from ESIA REST services.
 */
public class EsiaInformationConnector {

    /**
     * @param access_token access token
     * @param oid ESIA object id
     * @param properties connector settings
     */
    public EsiaInformationConnector(String access_token, long oid, EsiaProperties properties) {
        this.token = access_token;
        this.oid = oid;
        this.properties = properties;
        this._rest_base_url = String.format("%s/rs", properties.getServiceUrl());
    }

    /**
     * Makes request to ESIA REST service and returns response JSON data.
     *
     * @param endpoint_url endpoint url
     * @param accept_schema optional schema (version) for response data format
     * @return
     * @throws @{IncorrectJsonError} if response contains invalid json body
     * @throws @{HttpError} if response status code is not 2XX
     */
    public Map<String, Object> esia_request(String endpoint_url, String accept_schema) {
        final Map<String, String> headers = new TreeMap<String, String>() {{
            put("Authorization", String.format("Bearer %s", token));
        }};

        if (accept_schema != null) {
            headers.put("Accept", String.format("application/json; schema=\"%s\"", accept_schema));
        } else {
            headers.put("Accept", "application/json");
        }

        return EsiaUtils.make_request(endpoint_url, "GET", headers, null);
    }

    public Map<String, Object> get_person_main_info(String accept_schema) {
        final String url = String.format("%s/prns/%d", _rest_base_url, oid);
        return esia_request(url, accept_schema);
    }

    public Map<String, Object> get_person_addresses(String accept_schema) {
        final String url = String.format("%s/prns/%d/addrs?embed=(elements)", _rest_base_url, oid);
        return esia_request(url, accept_schema);
    }

    public Map<String, Object> get_person_contacts(String accept_schema) {
        final String url = String.format("%s/prns/%d/ctts?embed=(elements)", _rest_base_url, oid);
        return esia_request(url, accept_schema);
    }

    public Map<String, Object> get_person_documents(String accept_schema) {
        final String url = String.format("%s/prns/%d/docs?embed=(elements)", _rest_base_url, oid);
        return esia_request(url, accept_schema);
    }

    public Map<String, Object> get_person_orgs(String accept_schema) {
        final String url = String.format("%s/prns/%d/orgs?embed=(elements)", _rest_base_url, oid);
        return esia_request(url, accept_schema);
    }

    public long getOid() {
        return oid;
    }

    public final String token;
    public final long oid;
    public final EsiaProperties properties;
    protected final String _rest_base_url;
}
