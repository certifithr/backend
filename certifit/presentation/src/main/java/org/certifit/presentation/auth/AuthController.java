package org.certifit.presentation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.auth.AuthService;
import org.certifit.application.auth.command.SigninCommand;
import org.certifit.application.auth.command.SignupCommand;
import org.certifit.application.auth.result.AuthResult;
import org.certifit.presentation.auth.dto.AuthResponse;
import org.certifit.presentation.auth.dto.SigninRequest;
import org.certifit.presentation.auth.dto.SignupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Signup and signin endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        AuthResult result = authService.signup(new SignupCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.role()
        ));
        return toResponse(result);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate and receive a JWT")
    public AuthResponse signin(@Valid @RequestBody SigninRequest request) {
        AuthResult result = authService.signin(new SigninCommand(
                request.email(),
                request.password()
        ));
        return toResponse(result);
    }

    private AuthResponse toResponse(AuthResult result) {
        return new AuthResponse(
                result.token(),
                result.userId(),
                result.email(),
                result.firstName(),
                result.lastName(),
                result.role()
        );
    }
}
