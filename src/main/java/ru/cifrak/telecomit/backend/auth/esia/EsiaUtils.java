package ru.cifrak.telecomit.backend.auth.esia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.web.util.UriUtils;
import ru.cifrak.telecomit.backend.auth.esia.error.HttpError;
import ru.cifrak.telecomit.backend.auth.esia.error.IncorrectJsonError;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EsiaUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // TODO implement with java
    public static byte[] signData(byte[] data, X509Certificate signingCertificate, PrivateKey signingKey) throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
//        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(signingKey);
//        cmsGenerator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(contentSigner, signingCertificate));

        // TODO refactor this
        /* // for rsa key
        cmsGenerator.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC").build("SHA256withRSA", signingKey, signingCertificate));
        /*/ // for gost key
        cmsGenerator.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC").build("GOST3411WITHECGOST3410-2012-256", signingKey, signingCertificate));
        /**/
        cmsGenerator.addCertificates(new JcaCertStore(Arrays.asList(signingCertificate)));

        return cmsGenerator.generate(new CMSProcessableByteArray(data), true).getEncoded();
    }

    public static String get_timestamp() {
        //return datetime.datetime.now(pytz.utc).strftime('%Y.%m.%d %H:%M:%S %z').strip()
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss Z").withZone(ZoneOffset.UTC).format(Instant.now()).trim();
    }

    /**
     * Signs params adding client_secret key, containing signature based on
     * `scope`, `timestamp`, `client_id` and `state` keys values.
     * @param params requests parameters
     * @param certificate_file path to certificate file
     * @param private_key_file path to private key file
     * @return signed request parameters
     */
    public static Map<String, String> sign_params(
            Map<String, String> params,
            String certificate_file,
            String private_key_file) throws CertificateException, NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, CMSException, OperatorCreationException {
        final String plaintext = String.format("%s%s%s%s",
                params.getOrDefault("scope", ""),
                params.getOrDefault("timestamp", ""),
                params.getOrDefault("client_id", ""),
                params.getOrDefault("state", "")
        );

        /*
        final String uuid_str = UUID.randomUUID().toString();

        final File source_file = File.createTempFile(uuid_str, ".tmp.raw");
        final Path source_path = Paths.get(source_file.getAbsolutePath());

        final File destination_file = File.createTempFile(uuid_str, ".tmp.enc");
        final Path destination_path = Paths.get(destination_file.getAbsolutePath());

        Files.write(source_path, plaintext.getBytes());

        final String cmd = String.format(
                "openssl smime -sign -md sha256 -in %s -signer %s -inkey %s -out %s -outform DER",
                source_path,
                certificate_file,
                private_key_file,
                destination_path
                );

        final int res = Runtime.getRuntime().exec(cmd).waitFor();
        if (res != 0) {
            log.warn("openssl return {}", res);
            throw new IOException("sign error");
        }

        final byte[] bytes = Files.readAllBytes(destination_path);

        Files.deleteIfExists(source_path);
        Files.deleteIfExists(destination_path);

        */

        final CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");

        final X509Certificate signCert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(certificate_file));

        // TODO refactor this
        /* // for rsa key
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        /*/ // for gost key
        final KeyFactory keyFactory = KeyFactory.getInstance("ECGOST3410-2012", "BC");
        /**/

//        final PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(keyFilePath))));

        // TODO refactor this
        /* // for rsa pem key format
        byte[] kfb = new PemReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(private_key_file))))).readPemObject().getContent();
        /*/ // for gost DER key format
        byte[] kfb = Files.readAllBytes(Paths.get(private_key_file));;
        /**/

        final PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(kfb));

        final byte[] bytes = signData(plaintext.getBytes(), signCert, privateKey);

        params.put("client_secret", Base64.getUrlEncoder().encodeToString(bytes));

        return params;
    }

    public static String urlencode(Map<String, String> params) {
        return params.keySet().stream()
                .map(key -> key + "=" + UriUtils.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&", "", ""));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Makes request to given url and returns parsed response JSON
     *
     * @param url
     * @param method
     * @param headers
     * @param data
     * @throws @{HttpError} if requests.HTTPError occurs
     * @throws @{IncorrectJsonError} if response data cannot be parsed to JSON
     */
    public static Map<String, Object> make_request(String url, String method, Map<String, String> headers, Map<String, String> data) {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);

        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                rb = rb.addHeader(e.getKey(), e.getValue());
            }
        }

        if (data != null) {
            for (Map.Entry<String, String> e : data.entrySet()) {
                rb = rb.addParameter(e.getKey(), e.getValue());
            }
        }

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            try (CloseableHttpResponse response = client.execute(rb.build())) {
                String json_str = EntityUtils.toString(response.getEntity());
                log.debug("<<< {}", json_str);
                TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json_str, typeRef);
            }
        } catch (JsonProcessingException e) {
            throw new IncorrectJsonError(e);
        } catch (IOException e) {
            throw new HttpError(e);
        }
    }
}
