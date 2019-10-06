package io.github.starrybleu.sideproject0.auth;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@ToString(callSuper = true)
public class AuthenticatedUser extends User {

    private final ApiUser apiUser;

    AuthenticatedUser(ApiUser apiUser) {
        super(apiUser.getEmail(),
                apiUser.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(apiUser.getUserType().name()))
        );
        this.apiUser = apiUser;
    }

    public boolean isPassenger() {
        return apiUser.getUserType() == ApiUser.UserType.passenger;
    }

    public boolean isDriver() {
        return apiUser.getUserType() == ApiUser.UserType.driver;
    }

    public Integer getId() {
        return apiUser.getId();
    }
}
