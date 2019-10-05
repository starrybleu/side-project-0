package io.github.starrybleu.sideproject0.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.CONFLICT)
public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException(String message) {
        super(message);
        log.warn("", this);
    }
}
