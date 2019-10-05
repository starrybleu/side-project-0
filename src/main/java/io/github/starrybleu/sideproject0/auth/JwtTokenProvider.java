package io.github.starrybleu.sideproject0.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expires-after-creation-in-millis}")
    Long expiresAfterCreationInMillis;

    @Value("${jwt.secret}")
    String secret;

    private final JWTVerifier jwtVerifier;
    private final UserDetailsService userDetailsService;
    private final Algorithm algorithm;

    public JwtTokenProvider(JWTVerifier jwtVerifier, @Qualifier("taxiAppUserDetailService") UserDetailsService userDetailsService, Algorithm algorithm) {
        this.jwtVerifier = jwtVerifier;
        this.userDetailsService = userDetailsService;
        this.algorithm = algorithm;
    }

    public String createToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private String createToken(String email, Collection<? extends GrantedAuthority> roles) {
        Date now = new Date();
        return JWT.create()
                .withSubject(email)
                .withArrayClaim("roles", roles.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new)
                )
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expiresAfterCreationInMillis))
                .sign(algorithm);
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getEmailFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    String getToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    boolean validateToken(String jwtToken) {
        try {
            DecodedJWT decoded = jwtVerifier.verify(jwtToken);
            return decoded.getExpiresAt().after(new Date());
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

}
