package travel-thirdparty-touroperator-service-adapter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import travel-thirdparty-touroperator-service-adapter.exception.ReservationException;
import travel-thirdparty-touroperator-service-adapter.processor.SiteMinderRequestResponseProcessor;
import java.util.Map;

/**
 * Controller for handling SiteMinder reservation requests.
 * <p>
 * This controller receives incoming XML-based reservation requests,
 * forwards them to the appropriate processor, and returns a formatted XML response.
 * It also handles various types of errors by generating structured XML error responses.
 * </p>
 */
@RestController
@RequestMapping("/api/hotel/siteminder")
public class SiteMinderRequestController {

    private static final Logger logger = LoggerFactory.getLogger(SiteMinderRequestController.class);

    @Autowired
    private SiteMinderRequestResponseProcessor siteMinderProcessor;

    /**
     * Handles a reservation request by forwarding it to the processor and returning the response.
     *
     * @param xmlRequest The XML request body received.
     * @param headers    The HTTP headers from the request.
     * @return ResponseEntity containing the XML response or an error message in XML format.
     */
    @PostMapping(value = "/process", consumes = "application/xml", produces = "application/xml")
    public ResponseEntity<String> handleReservation(
            @RequestBody String xmlRequest,
            @RequestHeader Map<String, String> headers) {
        logger.info("Processing SiteMinder reservation request");
        
        logRequestDetails(xmlRequest, headers);
        try {
            long startTime = System.currentTimeMillis();
            String xmlResponse = siteMinderProcessor.processReservation(xmlRequest, headers);
         // Log the response received from the third-party service
            logger.info("Response received from third-party service: {}", xmlResponse);
            logProcessingTime(startTime);
            return ResponseEntity.ok(xmlResponse); // Return successful response
        } catch (ReservationException re) {
            return handleReservationError(re);
        } catch (Exception e) {
            return handleUnexpectedError(e);
        }
    }

    /**
     * Logs the incoming XML request and all request headers.// Log the incoming XML request
        logger.info("Received XML Request: {}", xmlRequest);
     *
     * @param xmlRequest The XML request body received.
     * @param headers    The headers from the HTTP request.
     */
    private void logRequestDetails(String xmlRequest, Map<String, String> headers) {
        logger.info("Incoming XML Request: {}", xmlRequest);
        headers.forEach((key, value) ->
                logger.info("Request Header: {} = {}", key, value));
    }

    /**
     * Logs the total processing time after the request completes.
     *
     * @param startTime The timestamp when the request started processing.
     */
    private void logProcessingTime(long startTime) {
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        logger.info("Request processed in {} seconds", duration);
    }

    /**
     * Handles ReservationException and returns an appropriate XML error response.
     *
     * @param e The ReservationException that occurred during processing.
     * @return ResponseEntity containing a structured XML error response.
     */
    private ResponseEntity<String> handleReservationError(ReservationException e) {
        logger.error("Reservation processing failed", e);
        return buildErrorResponse(
                HttpStatus.valueOf(e.getHttpStatus()),
                "SiteMinderError",
                e.getMessage(),
                e.getCause(),
                e);
    }

    /**
     * Handles unexpected exceptions and returns a generic internal server error response.
     *
     * @param e The exception that occurred.
     * @return ResponseEntity containing a structured XML error response.
     */
    private ResponseEntity<String> handleUnexpectedError(Exception e) {
        logger.error("Unexpected error processing reservation", e);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "InternalError",
                "An unexpected error occurred",
                e.getCause(),
                e);
    }

    /**
     * Builds a structured XML error response based on the provided details.
     *
     * @param status     The HTTP status code to return.
     * @param type       A brief identifier for the error type.
     * @param message    A human-readable description of the error.
     * @param cause      The root cause of the error.
     * @param exception  The full exception object used to extract stack trace.
     * @return ResponseEntity containing the XML-formatted error response.
     */
    private ResponseEntity<String> buildErrorResponse(
            HttpStatus status, String type,
            String message, Throwable cause, Exception exception) {
        String errorXml = buildErrorXml(status, type, message, cause, exception);
        return ResponseEntity.status(status).body(errorXml);
    }

    /**
     * Constructs an XML-formatted error response with detailed subsections.
     * Includes:
     * - Error code
     * - Error type
     * - Human-readable message
     * - Cause message
     * - Full stack trace
     *
     * @param status     The HTTP status code.
     * @param type       The type of error (e.g., InternalError, SiteMinderError).
     * @param message    The error message.
     * @param cause      The root cause of the error.
     * @param exception  The full exception object (used to extract the stack trace).
     * @return A well-formed XML error response as a String.
     */
    private String buildErrorXml(
            HttpStatus status, String type,
            String message, Throwable cause, Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<reservationResponse>\n");
        sb.append("  <error>\n");
        sb.append("    <code>").append(status.value()).append("</code>\n");
        sb.append("    <type>").append(escapeXml(type)).append("</type>\n");
        sb.append("    <message>")
          .append(message != null ? escapeXml(message) : "An unexpected error occurred")
          .append("</message>\n");
        sb.append("    <cause>")
          .append(cause != null ? escapeXml(cause.getMessage()) : "No cause available")
          .append("</cause>\n");
        sb.append("    <stackTrace>").append(escapeXml(getStackTrace(exception))).append("</stackTrace>\n");
        sb.append("  </error>\n");
        sb.append("</reservationResponse>");
        return sb.toString();
    }

    /**
     * Escapes special characters in a string to ensure valid XML output.
     *
     * @param input The string to escape.
     * @return A properly escaped XML-safe string.
     */
    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "<")
                   .replace(">", ">")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Extracts the full stack trace from an exception.
     *
     * @param exception The exception object.
     * @return The full stack trace as a string.
     */
    private String getStackTrace(Exception exception) {
        if (exception == null) return "";
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
