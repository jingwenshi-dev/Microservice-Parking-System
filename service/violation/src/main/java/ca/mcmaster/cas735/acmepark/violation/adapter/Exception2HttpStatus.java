package ca.mcmaster.cas735.acmepark.violation.adapter;

import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Exception2HttpStatus {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(NotFoundException e) {
        // Translate CustomerNotFoundException to Http 404 - NOT FOUND status code
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        // Translate IllegalArgumentException to Http 422 - UNPROCESSABLE CONTENT status code
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
