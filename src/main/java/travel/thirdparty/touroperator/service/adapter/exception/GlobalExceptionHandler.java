package travel.thirdparty.touroperator.service.adapter.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException ex) {
        return createErrorResponse(ex.getHttpStatus(), "ThirdPartyCommunicationError", ex.getMessage(), ex.getCause());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex) {
        return createErrorResponse(ex.getHttpStatus(), "InvalidInputError", ex.getMessage(), ex.getCause());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
        return createErrorResponse(ex.getHttpStatus(), "ServerError", ex.getMessage(), ex.getCause());
    }

    private ResponseEntity<String> createErrorResponse(int statusCode, String errorType, String errorMessage, Throwable cause) {
        // Include the root cause if available
        if (cause != null) {
            errorMessage += " (Root Cause: " + cause.getMessage() + ")";
        }

        // Create the error response with the numeric HTTP status code
        String responseBody = "<reservationResponse status=\"N\">" +
                              "<error>" +
                              "<code>" + statusCode + "</code>" +
                              "<type>" + errorType + "</type>" +
                              "<message>" + errorMessage + "</message>" +
                              "</error>" +
                              "</reservationResponse>";

        // Return the response with the appropriate HTTP status code
        return ResponseEntity.status(statusCode).body(responseBody);
    }
}
