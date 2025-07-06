package travel.thirdparty.touroperator.service.adapter.exception;

public class ReservationException extends RuntimeException {
    private final int httpStatus;
    private final String rootCause; // Optional field for root cause

    // Constructor with message and HTTP status
    public ReservationException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.rootCause = null; // No root cause in this constructor
    }

    // Constructor with message, HTTP status, and cause
    public ReservationException(int httpStatus, String message, Throwable cause) {
        super(message, cause); // Pass the cause to the parent class
        this.httpStatus = httpStatus;
        this.rootCause = cause != null ? cause.getMessage() : null; // Extract root cause message
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getRootCause() {
        return rootCause;
    }
}
