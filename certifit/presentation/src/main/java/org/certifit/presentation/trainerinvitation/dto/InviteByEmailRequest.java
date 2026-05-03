package org.certifit.presentation.trainerinvitation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteByEmailRequest(
        @NotBlank @Email String email
) {}
