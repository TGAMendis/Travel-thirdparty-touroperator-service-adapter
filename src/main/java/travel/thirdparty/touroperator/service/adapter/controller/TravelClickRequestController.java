package travel.thirdparty.touroperator.service.adapter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.thirdparty.touroperator.service.adapter.exception.ReservationException;
import travel.thirdparty.touroperator.service.adapter.processor.TravelClickRequestResponseProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hotel/travelclick")
public class TravelClickRequestController {

    private static final Logger logger = LoggerFactory.getLogger(TravelClickRequestController.class);

    @Autowired
    private TravelClickRequestResponseProcessor travelClickProcessor;

    /**
     * Handles a reservation request by forwarding it to the processor and returning the response.
     *
     * @param xmlRequest The XML string representing the reservation request.
     * @param headers    The headers included in the request.
     * @return The XML string representing the reservation response or an error response in XML format.
     */
    @PostMapping(value = "/process", consumes = "application/xml", produces = "application/xml")
    public ResponseEntity<String> handleReservation(@RequestBody String xmlRequest, @RequestHeader Map<String, String> headers) {
        logger.info("TravelClickRequestController - Processing reservation request");

        // Log the incoming XML request
        logger.info("Received XML Request: {}", xmlRequest);

        long startTime = System.currentTimeMillis();

        try {
            // Process the reservation request using the processor
            String xmlResponse = travelClickProcessor.processReservation(xmlRequest, headers);

            // Log the response received from the third-party service
            logger.info("Response received from third-party service: {}", xmlResponse);

            double processingTime = (System.currentTimeMillis() - startTime) / 1000.0;
            logger.info("TravelClickRequestController - Reservation processing completed in {} seconds", processingTime);
            
            
            return ResponseEntity.ok(xmlResponse); // Return successful response
        } catch (ReservationException e) {
            // Handle reservation-specific errors (Custom HTTP status code)
            logger.error("Reservation error: ", e);
            return buildErrorResponse(
                HttpStatus.valueOf(e.getHttpStatus()), // Use the status code from ReservationException
                "ThirdPartyCommunicationError",
                e.getMessage(),
                e.getCause(),
                e
            );
        } catch (RuntimeException e) {
            // Handle runtime errors (HTTP 500 Internal Server Error)
            logger.error("Runtime error: ", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ServerError", e.getMessage(), e.getCause(), e);
        } catch (Exception e) {
            // Handle all other unexpected errors (HTTP 500 Internal Server Error)
            logger.error("Unexpected error: ", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "UnexpectedError", e.getMessage(), e.getCause(), e);
        }
    }

    /**
     * Builds an XML-formatted error response with detailed subsections, including the stack trace.
     *
     * @param status     The HTTP status code.
     * @param type       The type of error (e.g., InvalidInputError, ThirdPartyCommunicationError).
     * @param message    The error message.
     * @param cause      The root cause of the error.
     * @param exception  The full exception object (used to extract the stack trace).
     * @return A ResponseEntity containing the formatted XML error response.
     */
    private ResponseEntity<String> buildErrorResponse(HttpStatus status, String type, String message, Throwable cause, Exception exception) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<reservationResponse>\n");
        xmlBuilder.append("  <error>\n");
        xmlBuilder.append("    <code>").append(status.value()).append("</code>\n");
        xmlBuilder.append("    <type>").append(escapeXml(type)).append("</type>\n");
        xmlBuilder.append("    <message>").append(message != null ? escapeXml(message) : "An unexpected error occurred").append("</message>\n");
        xmlBuilder.append("    <cause>").append(cause != null ? escapeXml(cause.getMessage()) : "No cause available").append("</cause>\n");
        xmlBuilder.append("    <stackTrace>").append(escapeXml(getStackTrace(exception))).append("</stackTrace>\n");
        xmlBuilder.append("  </error>\n");
        xmlBuilder.append("</reservationResponse>");

        return ResponseEntity.status(status).body(xmlBuilder.toString());
    }

    /**
     * Escapes special XML characters to prevent malformed XML.
     *
     * @param input The input string to escape.
     * @return The escaped string.
     */
    private String escapeXml(String input) {
        if (input == null) {
            return "";
        }
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
        if (exception == null) {
            return "";
        }
        StringBuilder stackTraceBuilder = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            stackTraceBuilder.append(element.toString()).append("\n");
        }
        return stackTraceBuilder.toString();
    }
}
