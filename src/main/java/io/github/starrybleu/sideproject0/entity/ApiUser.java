package io.github.starrybleu.sideproject0.entity;

import io.github.starrybleu.sideproject0.api.request.UserCreateReqBody;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class ApiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    ApiUser() {
    }

    public static ApiUser create(UserCreateReqBody reqBody, String encodedPassword) {
        ApiUser entity = new ApiUser();
        entity.email = reqBody.getEmail();
        entity.password = encodedPassword;
        entity.userType = reqBody.getUserType();
        return entity;
    }

    public enum UserType {
        passenger, driver
    }

}
