package io.github.starrybleu.sideproject0.api;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@ToString(exclude = "password")
public class UserSignInReqBody {
    @Size(min = 1)
    @Email
    String email;

    @Size(min = 1)
    String password;
}
