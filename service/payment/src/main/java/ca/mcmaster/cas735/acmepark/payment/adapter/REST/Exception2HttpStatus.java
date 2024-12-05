package ca.mcmaster.cas735.acmepark.payment.adapter.REST;

import ca.mcmaster.cas735.acmepark.payment.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.InvalidDateException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.VoucherExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Exception2HttpStatus {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistingException.class)
    public ResponseEntity<String> handleConflict(AlreadyExistingException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(VoucherExpiredException.class)
    public ResponseEntity<String> handleVoucherExpired(VoucherExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDate(InvalidDateException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
