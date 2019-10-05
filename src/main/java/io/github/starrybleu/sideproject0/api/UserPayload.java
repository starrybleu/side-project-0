package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import lombok.Data;

@Data
public class UserPayload {
    Integer id;
    String email;
    ApiUser.UserType userType;

    public static UserPayload from(ApiUser entity) {
        UserPayload payload = new UserPayload();
        payload.id = entity.getId();
        payload.email = entity.getEmail();
        payload.userType = entity.getUserType();
        return payload;
    }
}
