package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString(exclude = "password")
public class UserCreateReqBody {
    @Size(min = 1)
    @Email
    String email;

    @Size(min = 1)
    String password;

    @NotNull
    ApiUser.UserType userType;
}
