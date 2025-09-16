package ua.foxminded.chyzhov.carrestservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;

public interface ModelService {

    ModelDto findById(Integer modelId);

    Page<ModelDto> findAll(Pageable pageable);

    Page<ModelDto> findAllByMakeId(Integer makeId, Pageable pageable);

    ModelDto save(ModelDto modelDto);

    ModelDto update(Integer modelId, ModelDto modelDto);

    void deleteById(Integer modelId);

    ModelDto saveByMakeName(String manufacturerName, String modelName);

    ModelDto findByMakeNameAndModelName(String manufacturerName, String modelName);
}
