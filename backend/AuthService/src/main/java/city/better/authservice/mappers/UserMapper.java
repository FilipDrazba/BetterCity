package city.better.authservice.mappers;

import city.better.authservice.dtos.RegisterRequestDto;
import city.better.authservice.dtos.UserInfoDto;
import city.better.authservice.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterRequestDto registerRequestDto);

    UserInfoDto toUserInfoDto(User user);
}
