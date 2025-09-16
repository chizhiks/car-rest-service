package ua.foxminded.chyzhov.carrestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.entity.Category;

import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Set<Category> toEntity(Set<CategoryDto> categories);
}
