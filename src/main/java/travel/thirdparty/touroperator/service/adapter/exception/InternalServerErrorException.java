package travel.thirdparty.touroperator.service.adapter.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends RuntimeException {
    private final int httpStatus;

    // Constructor with message and HTTP status
    public InternalServerErrorException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus.value();
    }

    // Constructor with message, HTTP status, and cause
    public InternalServerErrorException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause); // Pass the cause to the parent class
        this.httpStatus = httpStatus.value();
    }

    // Constructor with only message (default to INTERNAL_SERVER_ERROR)
    public InternalServerErrorException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Constructor with message and cause (default to INTERNAL_SERVER_ERROR)
    public InternalServerErrorException(String message, Throwable cause) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
