package io.github.starrybleu.sideproject0.service;

import io.github.starrybleu.sideproject0.api.request.UserCreateReqBody;
import io.github.starrybleu.sideproject0.api.request.UserSignInReqBody;
import io.github.starrybleu.sideproject0.api.exception.BadUserAccessException;
import io.github.starrybleu.sideproject0.api.exception.UserDuplicateException;
import io.github.starrybleu.sideproject0.auth.JwtTokenProvider;
import io.github.starrybleu.sideproject0.entity.ApiUser;
import io.github.starrybleu.sideproject0.entity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, @Qualifier("taxiAppUserDetailService") UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ApiUser createUser(UserCreateReqBody reqBody) {
        Optional<ApiUser> existingUser = repository.findByEmail(reqBody.getEmail());

        if (existingUser.isPresent()) {
            throw new UserDuplicateException(String.format("The given email(%s) had already signed up.", reqBody.getEmail()));
        }

        ApiUser createdApiUser = ApiUser.create(reqBody, passwordEncoder.encode(reqBody.getPassword()));
        return repository.save(createdApiUser);
    }

    @Transactional
    public String authenticate(UserSignInReqBody reqBody) {
        UserDetails user = userDetailsService.loadUserByUsername(reqBody.getEmail());
        boolean isPasswordMatch = passwordEncoder.matches(reqBody.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new BadUserAccessException(reqBody.getEmail());
        }
        return jwtTokenProvider.createToken(user);
    }
}
