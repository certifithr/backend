package org.certifit.application.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.auth.command.SigninCommand;
import org.certifit.application.auth.command.SignupCommand;
import org.certifit.application.auth.result.AuthResult;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public AuthResult signup(SignupCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use: " + command.email());
        }

        UserEntity user = new UserEntity();
        user.setEmail(command.email());
        user.setPasswordHash(passwordEncoder.encode(command.password()));
        user.setFirstName(command.firstName());
        user.setLastName(command.lastName());
        user.setRole(command.role());

        UserEntity saved = userRepository.save(user);
        log.info("New user registered: id={}, role={}", saved.getId(), saved.getRole());

        String token = jwtService.generate(saved.getId(), saved.getEmail(), saved.getRole());
        return toResult(saved, token);
    }

    @Transactional(readOnly = true)
    public AuthResult signin(SigninCommand command) {
        UserEntity user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        log.info("User signed in: id={}", user.getId());
        String token = jwtService.generate(user.getId(), user.getEmail(), user.getRole());
        return toResult(user, token);
    }

    private AuthResult toResult(UserEntity user, String token) {
        return new AuthResult(
                token,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
