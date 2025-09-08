package ua.foxminded.chyzhov.carrestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;

import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CarMapper {

    CarDto toDto(Car car);

    Car toEntity(CarDto carDto);

    ModelDto toDto(Model model);

    Model toEntity(ModelDto modelDto);

    MakeDto toDto(Make make);

    Make toEntity(MakeDto makeDto);

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Set<Category> toEntity(Set<CategoryDto> categories);
}
