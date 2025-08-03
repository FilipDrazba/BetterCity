package city.better.reportservice.dtos;

public record ReportDto(
        Long id,
        String description,
        Double longitude,
        Double latitude
) {
}
