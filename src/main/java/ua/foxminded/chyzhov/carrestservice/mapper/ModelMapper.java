package ua.foxminded.chyzhov.carrestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.entity.Model;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ModelMapper {

    ModelDto toDto(Model model);

    Model toEntity(ModelDto modelDto);
}
