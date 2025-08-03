package city.better.authservice.mappers;

import city.better.authservice.dtos.JwtValueDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JwtMapper {
    JwtValueDto toJwtDto(String token);
}
