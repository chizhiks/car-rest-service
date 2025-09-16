package ua.foxminded.chyzhov.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.mapper.MakeMapper;
import ua.foxminded.chyzhov.carrestservice.mapper.ModelMapper;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.NotFoundException;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.RecordAlreadyExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final MakeRepository makeRepository;
    private final ModelMapper modelMapper;
    private final MakeMapper makeMapper;

    @Override
    public ModelDto findById(Integer modelId) {

        Model model = modelRepository.findById(modelId).orElseThrow(() -> new NotFoundException("Model", modelId));

        log.info("Model with modelId: {} found successfully", modelId);

        return modelMapper.toDto(model);
    }

    @Override
    public Page<ModelDto> findAll(Pageable pageable) {

        Page<Model> modelPage = modelRepository.findAll(pageable);

        log.info("{} models have been retrieved successfully", modelPage.getTotalElements());

        return modelPage.map(modelMapper::toDto);
    }

    @Override
    public Page<ModelDto> findAllByMakeId(Integer makeId, Pageable pageable) {

        Page<Model> modelPage = modelRepository.findAllByMakeMakeId(makeId, pageable);

        log.info("{} models have been retrieved successfully", modelPage.getTotalElements());

        return modelPage.map(modelMapper::toDto);
    }

    @Override
    public ModelDto findByMakeNameAndModelName(String manufacturerName, String modelName) {

        Model model = modelRepository.findByModelIgnoreCaseAndMakeMakeIgnoreCase(modelName, manufacturerName).orElseThrow(() -> new NotFoundException("Model", modelName));

        log.info("Model with modelName: {} found successfully", modelName);

        return modelMapper.toDto(model);
    }

    @Override
    @Transactional
    public ModelDto save(ModelDto modelDto) {

        if (modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase(modelDto.model(), modelDto.make().make())) {
            log.error("Model with modelName: {} and make: {} already exists", modelDto.model(), modelDto.make().make());
            throw new RecordAlreadyExists("Model", "modelName", modelDto.model());
        }

        Model model = modelMapper.toEntity(modelDto);

        Model savedModel = modelRepository.save(model);

        log.info("Model with modelId: {} was successfully saved", savedModel.getModelId());

        return modelMapper.toDto(savedModel);
    }

    @Override
    @Transactional
    public ModelDto saveByMakeName(String makeName, String modelName) {

        Make make = makeRepository.findByMakeIgnoreCase(makeName)
                .orElseGet(() -> {
                    Make newMake = new Make();
                    newMake.setMake(makeName);
                    return makeRepository.save(newMake);
                });

        if (modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase(modelName, makeName)) {
            log.error("Model with modelName: {} and make: {} already exists", modelName, makeName);
            throw new RecordAlreadyExists("Model", "modelName", modelName);
        }

        Model model = new Model();
        model.setModel(modelName);
        model.setMake(make);

        Model savedModel = modelRepository.save(model);
        log.info("Model with modelId: {} was successfully saved", savedModel.getModelId());

        return modelMapper.toDto(savedModel);
    }

    @Override
    @Transactional
    public ModelDto update(Integer modelId, ModelDto modelDto) {

        Model model = modelRepository.findById(modelId).orElseThrow(() -> new NotFoundException("Model", modelId));

        model.setMake(makeMapper.toEntity(modelDto.make()));
        model.setModel(modelDto.model());

        Model updatedModel = modelRepository.save(model);

        log.info("Model with modelId: {} was successfully updated", updatedModel.getModelId());

        return modelMapper.toDto(updatedModel);
    }

    @Override
    @Transactional
    public void deleteById(Integer modelId) {
        try {
            modelRepository.deleteById(modelId);
            log.info("Model with modelId: {} was successfully deleted", modelId);
        } catch (Exception e) {
            log.error("Error deleting model with modelId: {}", modelId, e);
            throw new NotFoundException("Model", modelId);
        }
    }
}
