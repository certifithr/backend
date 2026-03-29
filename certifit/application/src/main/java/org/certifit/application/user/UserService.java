package org.certifit.application.user;

import lombok.RequiredArgsConstructor;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserById(UUID uuid){

        return userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user exists with ID: " + uuid));
    }
}
