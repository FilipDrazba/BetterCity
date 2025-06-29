package city.better.reportservice.mapper;

import city.better.reportservice.dto.ReportDto;
import city.better.reportservice.dto.ReportDtoRequest;
import city.better.reportservice.model.Report;
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
