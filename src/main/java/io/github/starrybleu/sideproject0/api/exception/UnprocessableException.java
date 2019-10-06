package io.github.starrybleu.sideproject0.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnprocessableException extends RuntimeException {
    public UnprocessableException(String message) {
        super(message);
        log.warn("", this);
    }
}
