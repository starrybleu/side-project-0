package io.github.starrybleu.sideproject0;

import io.github.starrybleu.sideproject0.auth.AuthenticatedUser;
import io.github.starrybleu.sideproject0.entity.ApiUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Profile("test")
@TestConfiguration
public class MockUserTestConfig {

    private ApiUser passenger = mock(ApiUser.class);

    private ApiUser driver = mock(ApiUser.class);

    @Bean("taxiAppUserDetailService")
    @Primary
    public UserDetailsService userDetailsService() {
        when(passenger.getEmail())
                .thenReturn("passenger@company.com");
        when(passenger.getPassword())
                .thenReturn("password");
        when(passenger.getUserType())
                .thenReturn(ApiUser.UserType.passenger);
        when(passenger.getId())
                .thenReturn(1);

        when(driver.getEmail())
                .thenReturn("driver@company.com");
        when(driver.getPassword())
                .thenReturn("password");
        when(driver.getUserType())
                .thenReturn(ApiUser.UserType.driver);
        when(driver.getId())
                .thenReturn(9);

        return username -> {
            if (username.equalsIgnoreCase("passenger@company.com")) {
                return new AuthenticatedUser(passenger);
            } else if (username.equalsIgnoreCase("driver@company.com")) {
                return new AuthenticatedUser(driver);
            }
            throw new UsernameNotFoundException("Username cannot be found");
        };
    }

}
