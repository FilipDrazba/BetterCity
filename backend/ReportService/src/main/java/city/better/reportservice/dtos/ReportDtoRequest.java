package city.better.reportservice.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ReportDtoRequest(
        @NotNull
        String description,

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitude,

        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitude
) {
}
