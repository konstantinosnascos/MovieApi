package com.learn2earn.movie_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//för att det ska fungera, ändra MovieNotFoundException till(String message) och (message)
//    @ExceptionHandler(MovieNotFoundException.class)
//    public ResponseEntity<Object> handleMovieNotFound(MovieNotFoundException ex) {
//        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFound(MovieNotFoundException ex, HttpServletRequest request) {
        //format JSON error mapping 404
        //fortsätter med mitt tidigare format
        logger.warn("Movie not found: {}, path={}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        //return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DirectorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDirectorNotFound(DirectorNotFoundException ex, HttpServletRequest request) {
        //format JSON error mapping 404
        //fortsätter med mitt tidigare format
        logger.warn("Director not found: {}, path={}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        //return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieAlreadyLoanedException.class)
    public ResponseEntity<ErrorResponse> handleMovieAlreadyLoaned(MovieAlreadyLoanedException ex, HttpServletRequest request) {
        //format JSON error mapping 409
        //fortsätter med mitt tidigare format
        logger.warn("Movie already loaned: {}, path={}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(409, "Already loaned", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        //return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoanNotFound(LoanNotFoundException ex, HttpServletRequest request) {
        logger.warn("Loan not found: {}, path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MovieAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleMovieAlreadyReturned(MovieAlreadyReturnedException ex, HttpServletRequest request) {
        logger.warn("Movie already returned: {}, path={}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(409, "Conflict", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
