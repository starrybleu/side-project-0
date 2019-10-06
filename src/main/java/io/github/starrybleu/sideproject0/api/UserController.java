package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.api.request.UserCreateReqBody;
import io.github.starrybleu.sideproject0.api.request.UserSignInReqBody;
import io.github.starrybleu.sideproject0.api.response.UserPayload;
import io.github.starrybleu.sideproject0.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/api/users/sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserPayload signUp(@Valid @RequestBody UserCreateReqBody reqBody) {
        log.info("signUp reqBody: {}", reqBody);
        return UserPayload.from(userService.createUser(reqBody));
    }

    @PostMapping(value = "/api/users/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String signIn(@Valid @RequestBody UserSignInReqBody reqBody) {
        log.info("signIn email: {}", reqBody.getEmail());
        return userService.authenticate(reqBody);
    }

}
