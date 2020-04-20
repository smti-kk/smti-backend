package ru.cifrak.telecomit.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.cifrak.telecomit.backend.auth.ProjectUserDetails;
import ru.cifrak.telecomit.backend.auth.entity.User;
import ru.cifrak.telecomit.backend.auth.service.UserService;

import java.util.Optional;

@Component
public class TokenUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public TokenUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        token = removeStart(token, "Token").trim();

        final Optional<User> optionalUser = userService.findByToken(token);

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("Cannot find user with authentication token=" + token);
        }

        final User user = optionalUser.get();
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException(String.format("User '%s' is inactive. Access denied", user.getUsername()));
        }

        return new ProjectUserDetails(optionalUser.get());
    }

    private static String removeStart(String str, String remove) {
        if (str.isEmpty() || remove.isEmpty()) {
            return str;
        }
        if (str.startsWith(remove)){
            return str.substring(remove.length());
        }
        return str;
    }
}
