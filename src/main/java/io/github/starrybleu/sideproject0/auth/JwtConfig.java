package io.github.starrybleu.sideproject0.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    String secret;

    @Bean
    public JWTVerifier jwtVerifier() {
        return JWT.require(algorithm())
                .build();
    }

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }
}
