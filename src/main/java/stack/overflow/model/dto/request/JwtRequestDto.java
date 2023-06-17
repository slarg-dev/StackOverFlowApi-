package stack.overflow.model.dto.request;

import javax.validation.constraints.NotBlank;

public record JwtRequestDto(
        @NotBlank String username,
        @NotBlank String password) {
}
