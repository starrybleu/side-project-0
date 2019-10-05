package io.github.starrybleu.sideproject0.entity.repository;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApiUser, Integer> {
    Optional<ApiUser> findByEmail(String username);
}
