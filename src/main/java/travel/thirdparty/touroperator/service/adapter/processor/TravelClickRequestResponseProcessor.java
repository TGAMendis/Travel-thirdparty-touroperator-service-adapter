package travel-thirdparty-touroperator-service-adapter.processor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.net.SocketException;
import javax.net.ssl.SSLException;
import java.net.URL;
import travel-thirdparty-touroperator-service-adapter.exception.ReservationException; // Import custom exception
import travel-thirdparty-touroperator-service-adapter.dto.ReservationRequest;
import travel-thirdparty-touroperator-service-adapter.util.RestTemplateUtil;

@Service
public class TravelClickRequestResponseProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TravelClickRequestResponseProcessor.class);
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    public TravelClickRequestResponseProcessor() throws NoSuchAlgorithmException {
        this.restTemplate = RestTemplateUtil.createRestTemplateWithTlsLogging();
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        this.xmlMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Processes a reservation request by sending it to the third-party service and returning the response.
     *
     * @param xmlRequest The XML string representing the reservation request.
     * @param headers    The headers to include in the request.
     * @return The XML string representing the reservation response.
     * @throws ReservationException If an error occurs during processing.
     */
    public String processReservation(String xmlRequest, Map<String, String> headers) throws ReservationException {
        try {
            // Step 1: Convert XML request to ReservationRequest object
            ReservationRequest reservationRequest;
            try {
                reservationRequest = xmlMapper.readValue(xmlRequest, ReservationRequest.class);
                logger.info("Received reservation request: {}", reservationRequest);
            } catch (Exception e) {
                logger.error("Invalid XML request format: ", e);
                throw new ReservationException(400, "Invalid XML request format: " + e.getMessage(), e); // Pass original exception
            }

            // Step 2: Perform cipher suite check
            try {
                cipherSuiteChecker();
            } catch (Exception e) {
                logger.error("Cipher suite validation failed: ", e);
                throw new ReservationException(500, "Cipher suite validation failed: " + e.getMessage(), e); // Pass original exception
            }

            // Step 3: Log headers and extract BaseURL
            String baseUrl = extractAndValidateBaseUrl(headers);

            // Step 4: Send the reservation request to the third-party service
            try {
                String xmlResponse = sendReservationRequest(reservationRequest, xmlRequest, headers, baseUrl);
                logger.info("Generated reservation response: {}", xmlResponse);
                return xmlResponse;
            } catch (ReservationException re) {
                throw re; // Re-throw ReservationException as-is
            } catch (Exception e) {
                logger.error("Unexpected error while processing reservation: ", e);
                throw new ReservationException(500, "Internal Server Error", e); // Pass original exception
            }
        } catch (ReservationException re) {
            throw re; // Ensure ReservationException is propagated
        } catch (Exception e) {
            logger.error("Unexpected error in processReservation: ", e);
            throw new ReservationException(500, "Internal Server Error", e); // Pass original exception
        }
    }

    /**
     * Extracts and validates the BaseURL from the headers.
     *
     * @param headers The headers containing the BaseURL.
     * @return The validated BaseURL.
     * @throws ReservationException If the BaseURL is missing or invalid.
     */
    private String extractAndValidateBaseUrl(Map<String, String> headers) throws ReservationException {
        String baseUrl = null;
        // Log all headers received from the client
        logger.info("Headers received in Service Layer:");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            logger.info("{}: {}", key, value);
            if ("BaseURL".equalsIgnoreCase(key)) {
                baseUrl = value;
            }
        }
        logger.info("BaseURL: {}", baseUrl);
        // Validate BaseURL
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new ReservationException(400, "Third-party base URL is not configured.");
        }
        try {
            new URL(baseUrl); // Validate URL format
        } catch (Exception e) {
            throw new ReservationException(400, "Invalid third-party base URL: " + baseUrl, e); // Pass original exception
        }
        return baseUrl;
    }

    /**
     * Sends the reservation request to the third-party service.
     *
     * @param reservationRequest The ReservationRequest object to send.
     * @param xmlRequestOriginal The original XML request string.
     * @param headers            The headers to include in the request.
     * @param baseUrl            The validated BaseURL.
     * @return The XML string representing the reservation response.
     * @throws ReservationException If an error occurs while sending the request.
     */
    private String sendReservationRequest(ReservationRequest reservationRequest, String xmlRequestOriginal, Map<String, String> headers, String baseUrl) throws ReservationException {
        String url = baseUrl; // Use the validated BaseURL
        logger.info("Thirdparty TravelClick URL: {}", url);
        try {
            // Set headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_XML);
            httpHeaders.set("SOAPAction", "HotelResNotifRQ");
            httpHeaders.set("Accept-Encoding", "jgzip,deflate");
            httpHeaders.set("Cache-Control", "no-cache");
            httpHeaders.setConnection("keep-alive");

            // Log headers being sent to the third-party service
            logger.info("Headers being sent to Third-Party Service:");
            httpHeaders.forEach((key, value) -> logger.info("{}: {}", key, value));

            // Create the HTTP entity with the request body and headers
            HttpEntity<String> entity = new HttpEntity<>(xmlRequestOriginal, httpHeaders);

            // Send the request and receive the response as a String
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            logger.info("responseEntity.getBody(): {}", responseEntity.getBody());
            
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.info("Response received from third-party service: {}", responseEntity.getBody());
                return responseEntity.getBody();
            } else {
                logger.error("Third-party service returned non-2xx status code: {}", responseEntity.getStatusCode());
                throw new ReservationException(responseEntity.getStatusCodeValue(), "Third-party service error: " + responseEntity.getBody(), null);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            //logger.error("HTTP error while sending reservation request: ", e);
            logger.error("HTTP error while sending reservation request. Status Code: {}, Status Text: {}, Root Cause: {}",e.getRawStatusCode(), e.getStatusText(), e.getCause(), e);
            throw new ReservationException(e.getRawStatusCode(), "HTTP error: " + e.getStatusText(), e); // Pass original exception
        } catch (ResourceAccessException e) {
            // Handle network/SSL-related errors
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SocketException || rootCause instanceof SSLException) {
                //logger.error("Network/SSL error while communicating with third-party service: ", e);
                logger.error("Network/SSL error while communicating with third-party service. Root Cause: {}",rootCause != null ? rootCause.getMessage() : "Unknown", e);
                throw new ReservationException(502, "Bad Gateway: Unable to communicate with third-party service. Details: " + rootCause.getMessage(), e); // Pass original exception
            } else {
                //logger.error("Unexpected ResourceAccessException: ", e);
                logger.error("Unexpected ResourceAccessException. Root Cause: {}", rootCause != null ? rootCause.getMessage() : "Unknown", e);
                throw new ReservationException(504, "Gateway Timeout: Unable to complete the request to the third-party service.", e); // Pass original exception
            }
        } catch (RestClientException e) {
            //logger.error("Rest client error while sending reservation request: ", e);
            logger.error("Rest client error while sending reservation request. Root Cause: {}", e.getCause(), e);
            throw new ReservationException(502, "Bad Gateway: Unable to communicate with third-party service", e); // Pass original exception
        } catch (Exception e) {
            //logger.error("Unexpected error while sending reservation request: ", e);
            logger.error("Unexpected error while sending reservation request. Root Cause: {}", e.getCause(), e);
            throw new ReservationException(500, "Internal Server Error", e); // Pass original exception
        }
    }

    /**
     * Converts an InputStream to a String.
     *
     * @param inputStream The InputStream to convert.
     * @return The converted String.
     * @throws Exception If an error occurs during conversion.
     */
    public String convertStreamToString(InputStream inputStream) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("Start converting stream to string");
        StringBuilder sb = new StringBuilder();
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        }
        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;
        logger.info("Finished converting stream to string in {} seconds", duration);
        return sb.toString();
    }

    /**
     * Simulates a cipher suite check.
     */
    public void cipherSuiteChecker() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        String[] supportedCiphers = sslContext.getSupportedSSLParameters().getCipherSuites();
        logger.info("Supported Cipher Suites:");
        for (String cipher : supportedCiphers) {
            logger.info(cipher);
        }
    }
}
