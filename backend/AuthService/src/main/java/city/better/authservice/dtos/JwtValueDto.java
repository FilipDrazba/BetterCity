package city.better.authservice.dtos;

import jakarta.validation.constraints.NotBlank;

public record JwtValueDto(
        @NotBlank
        String token
) {

}
