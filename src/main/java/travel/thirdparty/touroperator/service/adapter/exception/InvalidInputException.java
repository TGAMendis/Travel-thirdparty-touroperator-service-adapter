package travel.thirdparty.touroperator.service.adapter;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends RuntimeException {
    private final int httpStatus;

    // Constructor with message and HTTP status
    public InvalidInputException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus.value();
    }

    // Constructor with message, HTTP status, and cause
    public InvalidInputException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause); // Pass the cause to the parent class
        this.httpStatus = httpStatus.value();
    }

    // Constructor with only message (default to BAD_REQUEST)
    public InvalidInputException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    // Constructor with message and cause (default to BAD_REQUEST)
    public InvalidInputException(String message, Throwable cause) {
        this(message, HttpStatus.BAD_REQUEST, cause);
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
