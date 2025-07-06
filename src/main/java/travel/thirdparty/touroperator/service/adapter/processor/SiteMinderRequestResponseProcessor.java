package travel-thirdparty-touroperator-service-adapter.processor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

// Required imports
import org.springframework.beans.factory.annotation.Autowired; // For @Autowired
import javax.net.ssl.SSLException;                         // For SSLException
import java.net.SocketException;                           // For SocketException
import travel-thirdparty-touroperator-service-adapter.exception.ReservationException; // Custom exception

/**
 * Processor class responsible for handling SiteMinder reservation requests.
 * <p>
 * This class receives XML data from the controller, processes it, sends it to the third-party SiteMinder service,
 * and returns a response. It also handles various types of errors (HTTP, network, SSL) and logs them appropriately.
 * </p>
 */
@Service
public class SiteMinderRequestResponseProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SiteMinderRequestResponseProcessor.class);
    private final RestTemplate siteMinderRestTemplate;
    private final XmlMapper xmlMapper;

    /**
     * Constructor initializes a configured RestTemplate and XML mapper.
     *
     * @throws NoSuchAlgorithmException if TLS algorithm is not supported
     */
    @Autowired
    public SiteMinderRequestResponseProcessor() throws NoSuchAlgorithmException {
        this.siteMinderRestTemplate = createSiteMinderRestTemplate();
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        this.xmlMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        logger.info("SiteMinderRequestResponseProcessor initialized successfully.");
    }

    /**
     * Processes an incoming SiteMinder reservation request by forwarding it to the third-party system.
     *
     * @param xmlRequest The raw XML request string.
     * @param headers    The HTTP headers received with the request.
     * @return A response XML string from the third-party system.
     * @throws ReservationException If any error occurs during processing.
     */
    public String processReservation(String xmlRequest, Map<String, String> headers) throws ReservationException {
        try {
            logger.info("Starting reservation request processing.");
            logHeaders(headers);
            validateCipherSuites();

            String baseUrl = extractAndValidateBaseUrl(headers);
            logger.debug("Base URL extracted: {}", baseUrl);

            return sendToSiteMinder(xmlRequest, headers, baseUrl);
        } catch (ReservationException re) {
            logger.error("Reservation exception occurred", re);
            throw re;
        } catch (Exception e) {
            logger.error("Unexpected error during reservation processing", e);
            throw new ReservationException(500, "Internal Server Error", e);
        }
    }

    /**
     * Validates cipher suites used for TLS communication.
     *
     * @throws NoSuchAlgorithmException if TLS algorithm is not available
     */
    public void validateCipherSuites() throws NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        try {
            sslContext.init(null, null, null); // May throw KeyManagementException
        } catch (KeyManagementException e) {
            logger.error("Failed to initialize SSLContext", e);
            throw new RuntimeException("SSLContext initialization failed", e);
        }

        logger.info("Validating cipher suites for SiteMinder compatibility");

        Arrays.stream(sslContext.getSupportedSSLParameters().getCipherSuites())
              .forEach(cipher -> logger.debug("Supported Cipher: {}", cipher));
    }

    /**
     * Extracts and validates the BaseURL header from the request headers.
     *
     * @param headers The request headers.
     * @return The validated BaseURL.
     * @throws ReservationException If BaseURL is missing or invalid.
     */
    private String extractAndValidateBaseUrl(Map<String, String> headers) throws ReservationException {
        logger.debug("Extracting BaseURL from headers...");
        String baseUrl = headers.entrySet().stream()
                .peek(entry -> logger.debug("Header: {}={}", entry.getKey(), entry.getValue()))
                .filter(entry -> "BaseURL".equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new ReservationException(400, "BaseURL header is required"));

        try {
            new URL(baseUrl);
            logger.info("Validated BaseURL: {}", baseUrl);
            return baseUrl;
        } catch (Exception e) {
            logger.warn("Invalid BaseURL provided: {}", baseUrl, e);
            throw new ReservationException(400, "Invalid BaseURL: " + baseUrl, e);
        }
    }

    /**
     * Sends the XML request to the SiteMinder endpoint using the configured RestTemplate.
     *
     * @param xmlRequest The raw XML request string.
     * @param headers    The request headers.
     * @param baseUrl    The endpoint URL.
     * @return The XML response from SiteMinder.
     * @throws ReservationException If an HTTP or network error occurs.
     */
    private String sendToSiteMinder(String xmlRequest, Map<String, String> headers, String baseUrl) throws ReservationException {
        logger.info("Sending request to SiteMinder endpoint: {}", baseUrl);

        HttpHeaders httpHeaders = createSiteMinderHeaders(headers);
        
        //Hardcoded Value required for SiteMinder
        MediaType textXmlWithCharset = MediaType.valueOf("text/xml;charset=UTF-8");
        httpHeaders.setContentType(textXmlWithCharset);
        

        // Log the full request body
        logger.info("Request body being sent to SiteMinder:\n{}", xmlRequest);

        // Create HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(xmlRequest, httpHeaders);

        // Log all headers being sent
            logger.info("Headers being sent to SiteMinder:");
            httpHeaders.forEach((key, values) -> {
                logger.info("{}: {}", key, String.join(",", values));
            });


        // Optional: One-line summary of entity
        logger.info("Sending HttpEntity - Body Length: {} chars, Headers: {}", xmlRequest.length(), httpHeaders.size());

        try {
            ResponseEntity<String> response = siteMinderRestTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            logResponse(response);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            handleHttpError(e);
            return null; // unreachable
        } catch (ResourceAccessException e) {
            handleNetworkError(e);
            return null; // unreachable
        } catch (Exception e) {
            logger.error("Unexpected error communicating with SiteMinder", e);
            throw new ReservationException(500, "Internal Server Error", e);
        }
    }
    
    private String httpEntityToString(HttpEntity<String> entity) {
        StringBuilder sb = new StringBuilder();

        // Log headers
        sb.append("HTTP Headers:\n");
        if (entity.getHeaders() != null && !entity.getHeaders().isEmpty()) {
            entity.getHeaders().forEach((key, values) -> {
                sb.append(key).append(": ").append(String.join(",", values)).append("\n");
            });
        }

        // Log body
        sb.append("\nHTTP Body:\n");
        sb.append(entity.getBody() != null ? entity.getBody() : "[empty body]");

        return sb.toString();
    }

    /**
     * Creates and configures the HttpHeaders object for sending to SiteMinder.
     *
     * @param headers The incoming HTTP headers.
     * @return A fully configured HttpHeaders object.
     */
    private HttpHeaders createSiteMinderHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        httpHeaders.set("SOAPAction", "HotelResNotifRQ");
        httpHeaders.set("Accept-Encoding", "gzip,deflate");
        httpHeaders.setCacheControl("no-cache");
        httpHeaders.setConnection("keep-alive");

        headers.forEach((key, value) -> {
            if (key.toLowerCase().startsWith("x-")) {
                httpHeaders.add(key, value);
            }
        });

        logger.debug("Final headers for SiteMinder request:");
        httpHeaders.forEach((key, values) ->
                logger.debug("{}: {}", key, String.join(",", values)));

        return httpHeaders;
    }

    /**
     * Logs the response body and status code after receiving a response from SiteMinder.
     *
     * @param response The ResponseEntity returned from SiteMinder.
     */
    private void logResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("Non-successful response from SiteMinder: {}", response.getStatusCode());
        }
        logger.info("Received response from SiteMinder: {}", response.getBody());
    }

    /**
     * Handles HTTP errors returned by the SiteMinder service.
     *
     * @param e The HttpStatusCodeException thrown.
     * @throws ReservationException Wraps the HTTP error into a custom exception.
     */
    private void handleHttpError(HttpStatusCodeException e) throws ReservationException {
        logger.error("SiteMinder HTTP error - Status: {}, Body: {}", e.getRawStatusCode(), e.getResponseBodyAsString(), e);
        throw new ReservationException(
                e.getRawStatusCode(),
                "SiteMinder error: " + e.getStatusText(),
                e
        );
    }

    /**
     * Handles network-related exceptions such as connection issues or timeouts.
     *
     * @param e The ResourceAccessException thrown.
     * @throws ReservationException Wraps the network issue into a custom exception.
     */
    private void handleNetworkError(ResourceAccessException e) throws ReservationException {
        Throwable rootCause = e.getRootCause();
        String errorMsg = "Network error communicating with SiteMinder";

        if (rootCause instanceof SSLException) {
            errorMsg = "SSL Handshake failed with SiteMinder";
            logger.error("{} - {}", errorMsg, rootCause.getMessage(), e);
            throw new ReservationException(502, errorMsg + ": " + rootCause.getMessage(), e);
        } else if (rootCause instanceof SocketException) {
            errorMsg = "Connection error with SiteMinder";
            logger.error("{} - {}", errorMsg, rootCause.getMessage(), e);
            throw new ReservationException(504, errorMsg + ": " + rootCause.getMessage(), e);
        } else {
            logger.error("{} - {}", errorMsg, rootCause != null ? rootCause.getMessage() : "Unknown", e);
            throw new ReservationException(502, errorMsg, e);
        }
    }

    /**
     * Converts an InputStream to a String.
     *
     * @param inputStream The InputStream to convert.
     * @return The converted String.
     * @throws IOException If an I/O error occurs.
     */
    public String convertStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Generates a RestTemplate instance configured for SiteMinder communication.
     *
     * @return A configured RestTemplate.
     * @throws NoSuchAlgorithmException if TLS algorithm is not supported
     */
    private RestTemplate createSiteMinderRestTemplate() throws NoSuchAlgorithmException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.createDefault()))
                .build();

        logger.info("Created TLS-enabled RestTemplate for SiteMinder communication.");

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    /**
     * Logs all incoming request headers for debugging purposes.
     *
     * @param headers The request headers.
     */
    private void logHeaders(Map<String, String> headers) {
        logger.debug("Incoming request headers:");
        headers.forEach((key, value) -> logger.debug("{}: {}", key, value));
    }

    /**
     * Returns the stack trace of the given exception as a String.
     *
     * @param exception The exception to analyze.
     * @return The stack trace as a String.
     */
    private String getStackTrace(Exception exception) {
        if (exception == null) return "";
        return Arrays.stream(exception.getStackTrace())
                     .map(StackTraceElement::toString)
                     .collect(Collectors.joining("\n"));
    }
}
