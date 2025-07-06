package travel.thirdparty.touroperator.service.adapter.util;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class RestTemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    /**
     * Creates a general-purpose RestTemplate with TLS logging
     */
    public static RestTemplate createRestTemplateWithTlsLogging() throws NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        try {
            sslContext.init(null, null, null);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Failed to initialize SSLContext", e);
        }

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext) {
            @Override
            protected void prepareSocket(SSLSocket socket) {
                logger.info("Negotiated Protocol: {}", socket.getSession().getProtocol());
                logger.info("Negotiated Cipher Suite: {}", socket.getSession().getCipherSuite());
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    /**
     * Creates a SiteMinder-specific RestTemplate with optimized configuration
     */
    public static RestTemplate createSiteMinderRestTemplate() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, createTrustManagers(), new SecureRandom());

            // SiteMinder-specific SSL configuration
            SSLParameters sslParameters = new SSLParameters();
            sslParameters.setProtocols(new String[]{"TLSv1.2"});
            sslParameters.setCipherSuites(getSiteMinderCipherSuites());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    sslParameters.getProtocols(),
                    sslParameters.getCipherSuites(),
                    NoopHostnameVerifier.INSTANCE);

            // Configure connection pooling
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", socketFactory)
                    .build();

            PoolingHttpClientConnectionManager connectionManager = 
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(100);
            connectionManager.setDefaultMaxPerRoute(20);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .disableCookieManagement()
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory = 
                new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setConnectTimeout(10000);
            requestFactory.setReadTimeout(30000);

            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            logger.error("Failed to create SiteMinder RestTemplate", e);
            throw new RuntimeException("Failed to initialize SiteMinder RestTemplate", e);
        }
    }

    private static TrustManager[] createTrustManagers() {
        return new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
    }

    private static String[] getSiteMinderCipherSuites() {
        return new String[]{
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_RSA_WITH_AES_256_GCM_SHA384"
        };
    }
}
