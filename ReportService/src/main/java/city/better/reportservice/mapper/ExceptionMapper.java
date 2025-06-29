package city.better.reportservice.mapper;

import city.better.reportservice.dto.ExceptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    ExceptionDto toDto(Exception exception);
}
