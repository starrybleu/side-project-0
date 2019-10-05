package io.github.starrybleu.sideproject0.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Throwable> handleUncaughtException(Throwable t) {
        log.error("", t);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(t);
    }


}
