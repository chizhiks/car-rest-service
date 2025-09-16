package ua.foxminded.chyzhov.carrestservice.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record CarDto(

        @NotBlank(message = "Object ID should not be empty")
        @Size(max = 255, message = "Object ID cannot exceed 255 characters")
        String objectId,

        @NotNull(message = "Model is required")
        ModelDto model,

        @NotNull(message = "Year is required")
        @Positive(message = "Year must be positive")
        Integer year,

        Set<CategoryDto> categories
) {
}
