package ua.foxminded.chyzhov.carrestservice.dto;

public record CarFilterDto(
        String manufacturer,
        String model,
        Integer minYear,
        Integer maxYear,
        String category
) {
}
