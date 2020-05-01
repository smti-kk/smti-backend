package ru.cifrak.telecomit.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.cifrak.telecomit.backend.auth.ProjectUserDetails;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.auth.service.UserService;

import java.util.Optional;

@Component
public class UsernameUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public UsernameUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userService.findByUsername(username);

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("Cannot find user with username=" + username);
        }

        final User user = optionalUser.get();
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException(String.format("User '%s' is inactive. Access denied", user.getUsername()));
        }

        if (!user.getRoles().contains(UserRole.ADMIN)) {
            throw new UsernameNotFoundException(String.format("User '%s' is not have ADMIN role. Access denied", user.getUsername()));
        }

        return new ProjectUserDetails(optionalUser.get());
    }
}
