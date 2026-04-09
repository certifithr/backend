package org.certifit.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.certifit.application.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(exception, HttpStatus.NOT_FOUND, request));
    }

    private ErrorResponse buildErrorResponse(Exception exception, HttpStatus status, HttpServletRequest request){
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
    }
}
