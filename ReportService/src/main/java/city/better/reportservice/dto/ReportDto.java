package city.better.reportservice.dto;

public record ReportDto(
        Long id,
        String description,
        Double longitude,
        Double latitude
) {
}
