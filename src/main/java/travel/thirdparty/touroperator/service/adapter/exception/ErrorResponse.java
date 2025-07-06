package travel.thirdparty.touroperator.service.adapter.exception;

public class ErrorResponse {
    private int statusCode;
    private String type;
    private String message;
    private String rootCause; // Optional field for root cause

    // Constructor without root cause
    public ErrorResponse(int statusCode, String type, String message) {
        this.statusCode = statusCode;
        this.type = type;
        this.message = message;
    }

    // Constructor with root cause
    public ErrorResponse(int statusCode, String type, String message, String rootCause) {
        this.statusCode = statusCode;
        this.type = type;
        this.message = message;
        this.rootCause = rootCause;
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }
}
