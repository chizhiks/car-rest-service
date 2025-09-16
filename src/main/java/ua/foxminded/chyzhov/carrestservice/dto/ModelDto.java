package ua.foxminded.chyzhov.carrestservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModelDto(

        Integer modelId,

        @NotNull(message = "Make is required")
        MakeDto make,

        @NotBlank(message = "Model is required")
        @Size(max = 255, message = "Model name cannot exceed 255 characters")
        String model
) {
}
