package city.better.reportservice.dto;

public record ReportDtoRequest(
        String description,
        Double longitude,
        Double latitude
) {
}
