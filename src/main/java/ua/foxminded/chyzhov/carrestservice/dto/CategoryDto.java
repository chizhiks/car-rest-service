package ua.foxminded.chyzhov.carrestservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(

        Integer categoryId,

        @NotBlank(message = "Categoty is required")
        @Size(max = 255, message = "Category name cannot exceed 255 characters")
        String category
) {
}
