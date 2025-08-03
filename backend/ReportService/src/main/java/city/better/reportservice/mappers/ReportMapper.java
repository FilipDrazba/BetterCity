package city.better.reportservice.mappers;

import city.better.reportservice.dtos.ReportDto;
import city.better.reportservice.dtos.ReportDtoRequest;
import city.better.reportservice.entities.Report;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportDto toDto(Report report);

    List<ReportDto> toDtos(List<Report> reports);

    Report toEntity(ReportDtoRequest request);

    void updateReportFromPatch(@MappingTarget Report report,
                               ReportDtoRequest patch);
}
