package ru.cifrak.telecomit.backend.auth.service;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.cifrak.telecomit.backend.auth.ProjectUserDetails;
import ru.cifrak.telecomit.backend.auth.entity.User;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    Optional<User> findByToken(String token);

    Optional<User> findByOid(Long oid);

    static User getUser(Principal principal) {
        return ((ProjectUserDetails)((PreAuthenticatedAuthenticationToken) principal).getPrincipal()).getUser();
    }

    User save(User user);
}
