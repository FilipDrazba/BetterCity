package city.better.authservice.dtos;


import city.better.authservice.enums.Role;

public record UserInfoDto(Long id,
                          Role role) {

}
