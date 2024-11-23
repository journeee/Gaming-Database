package util;

import java.time.LocalDateTime;

public class ApiResponseFormat<T> {
    private boolean success;       // Indicates if the operation was successful
    private String message;        // Description of the operation result
    private T data;                // Data returned by the API (generic type)
    private String error;          // Error message in case of failure
    private int statusCode;        // HTTP status code for the response
    private String timestamp;      // Timestamp of the response

    // Constructor
    public ApiResponseFormat(boolean success, String message, T data, String error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now().toString(); // Automatically set the current timestamp
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Utility Method for Quick Success Response
    public static <T> ApiResponseFormat<T> success(T data, String message, int statusCode) {
        return new ApiResponseFormat<>(true, message, data, null);
    }

    // Utility Method for Quick Failure Response
    public static <T> ApiResponseFormat<T> failure(String error, String message, int statusCode) {
        return new ApiResponseFormat<>(false, message, null, error);
    }
}
