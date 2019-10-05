package io.github.starrybleu.sideproject0.auth;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import io.github.starrybleu.sideproject0.entity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("taxiAppUserDetailService")
public class TaxiAppUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public TaxiAppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser apiUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("The given email(%s) cannot be found.", username)));
        return new AuthenticatedUser(apiUser);
    }

}
