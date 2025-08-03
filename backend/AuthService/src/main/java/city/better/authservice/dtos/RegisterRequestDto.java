package city.better.authservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegisterRequestDto(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "^" +
                "(?=.*[A-Z])" +
                "(?=.*[a-z])" +
                "(?=.*\\d)" +
                "(?=.*[!@#$%^&*])" +
                "[A-Za-z\\d!@#$%^&*]{8,}" +
                "$")
        String password,

        @Pattern(regexp = "USER|UNVERIFIED_EMPLOYEE")
        String role
) {

}
