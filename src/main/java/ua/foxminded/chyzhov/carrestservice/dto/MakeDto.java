package ua.foxminded.chyzhov.carrestservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MakeDto(

        Integer makeId,

        @NotBlank(message = "Make is required")
        @Size(max = 255, message = "Make name cannot exceed 255 characters")
        String make
) {
}
