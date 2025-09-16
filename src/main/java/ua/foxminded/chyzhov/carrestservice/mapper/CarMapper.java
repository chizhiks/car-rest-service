package ua.foxminded.chyzhov.carrestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.entity.Car;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CarMapper {

    CarDto toDto(Car car);

    Car toEntity(CarDto carDto);
}
