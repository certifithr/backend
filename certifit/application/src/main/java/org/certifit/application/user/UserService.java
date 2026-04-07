package org.certifit.application.user;

import lombok.RequiredArgsConstructor;
import org.certifit.application.exception.UserNotFoundException;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserById(UUID uuid){
        return userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));
    }
}
