package io.github.starrybleu.sideproject0.api;

import io.github.starrybleu.sideproject0.entity.User;
import lombok.Data;

@Data
public class UserPayload {
    Integer id;
    String email;
    User.UserType userType;

    public static UserPayload from(User entity) {
        UserPayload payload = new UserPayload();
        payload.id = entity.getId();
        payload.email = entity.getEmail();
        payload.userType = entity.getUserType();
        return payload;
    }
}
