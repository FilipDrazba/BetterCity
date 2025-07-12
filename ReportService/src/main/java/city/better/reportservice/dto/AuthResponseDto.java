package city.better.reportservice.dto;

import city.better.reportservice.enums.Role;

public record AuthResponseDto(
        Long id,
        Role role
) {

}
