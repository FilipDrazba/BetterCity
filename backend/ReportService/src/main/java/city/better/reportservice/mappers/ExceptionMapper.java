package city.better.reportservice.mappers;

import city.better.reportservice.dtos.ExceptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    ExceptionDto toDto(Exception exception);
}
