package ua.foxminded.chyzhov.carrestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.entity.Make;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface MakeMapper {

    MakeDto toDto(Make make);

    Make toEntity(MakeDto makeDto);
}
