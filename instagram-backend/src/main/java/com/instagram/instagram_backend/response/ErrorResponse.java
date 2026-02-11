package com.instagram.instagram_backend.response;


import java.time.LocalDateTime;

public class ErrorResponse {

      private LocalDateTime timestamp;
      private String message;
      private int status;
      private String error;

    public ErrorResponse(LocalDateTime timestamp, String message, int status, String error) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public ErrorResponse() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
