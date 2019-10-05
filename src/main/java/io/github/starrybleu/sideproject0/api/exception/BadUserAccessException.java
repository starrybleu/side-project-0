package io.github.starrybleu.sideproject0.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadUserAccessException extends RuntimeException {
    public BadUserAccessException(String email) {
        super(String.format("Provided password for %s doesn't match.", email));
    }
}
