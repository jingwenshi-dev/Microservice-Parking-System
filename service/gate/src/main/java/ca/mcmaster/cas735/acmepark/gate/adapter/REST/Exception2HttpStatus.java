package ca.mcmaster.cas735.acmepark.gate.adapter.REST;


import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 标记该类为全局异常处理组件
@RestControllerAdvice
public class Exception2HttpStatus {


    // 处理 NotFoundException 异常
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(NotFoundException e) {
        // 将 NotFoundException 转换为 HTTP 404 - NOT FOUND 状态码
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    // 处理 IllegalArgumentException 异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        // 将 IllegalArgumentException 转换为 HTTP 422 - UNPROCESSABLE ENTITY 状态码
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

}
