package com.swcns.dopoonserver.domain.user.facade;

import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserRepository userRepository;

    public Optional<User> queryCurrentUser() {
        return queryCurrentUser(false);
    }

    public Optional<User> queryCurrentUser(boolean withPersistenced) {
        User found = (User)SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(withPersistenced) {
            return userRepository.findById(found.getId());
        }else {
            return Optional.of(found);
        }
    }

}
