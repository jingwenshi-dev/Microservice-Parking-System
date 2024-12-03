package ca.mcmaster.cas735.acmepark.gate.adapter.REST;


import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Mark this class as a global exception handling component
@RestControllerAdvice
public class Exception2HttpStatus {

    // Handling NotFoundException Exceptions
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(NotFoundException e) {
        // Converting NotFoundException to HTTP 404 - NOT FOUND Status Code
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
