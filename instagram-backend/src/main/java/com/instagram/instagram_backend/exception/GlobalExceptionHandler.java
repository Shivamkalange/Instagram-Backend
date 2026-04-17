package com.instagram.instagram_backend.exception;

import com.instagram.instagram_backend.response.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<?> handleInvalidActionException(InvalidActionException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(ex.getMessage());
        response.setStatus(400);
        response.setError("Invalid Action");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(ex.getMessage());
        response.setStatus(404);
        response.setError("User Not Found");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
//        ErrorResponse response = new ErrorResponse();
//        response.setTimestamp(LocalDateTime.now());
//        response.setMessage(ex.getMessage());
//        response.setStatus(401);
//        response.setError("Unauthorized");
//
//        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
//    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("You do not have permission to access this resource");
        response.setStatus(403);
        response.setError("Access Denied");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(403));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(ex.getMessage());
        response.setStatus(500);
        response.setError("Internal Server Error");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
    }
}

