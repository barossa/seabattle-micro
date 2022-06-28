package by.bsuir.seabattle.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex){
        // TODO: 28/06/2022 EXCEPTION HANDLING
        log.debug("Service exception handled: {}", ex.getClass());
        return ResponseEntity.badRequest().body("Service exception: " + ex.getClass());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("Internal exception handled: {}", ex.getClass());
        return ResponseEntity.badRequest().build();
    }
}
