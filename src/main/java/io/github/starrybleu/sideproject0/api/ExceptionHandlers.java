package io.github.starrybleu.sideproject0.api;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorHttpResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorHttpResponse(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHttpResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorHttpResponse(e));
    }

    @Data
    static class ErrorHttpResponse {
        String error;
        String message;

        ErrorHttpResponse(Throwable t) {
            this.error = t.getClass().getSimpleName();
            this.message = t.getMessage();
        }
    }


}
