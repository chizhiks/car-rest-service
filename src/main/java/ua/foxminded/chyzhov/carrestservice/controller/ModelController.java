package ua.foxminded.chyzhov.carrestservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public ResponseEntity<Page<ModelDto>> getAllModels(Pageable pageable) {

        Page<ModelDto> models = modelService.findAll(pageable);

        return ResponseEntity.ok(models);
    }

    @PostMapping
    public ResponseEntity<ModelDto> createModel(@Valid @RequestBody ModelDto modelDto) {

        ModelDto savedModel = modelService.save(modelDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedModel);
    }

    @GetMapping("/{modelId}")
    public ResponseEntity<ModelDto> getModelById(@PathVariable Integer modelId) {

        ModelDto model = modelService.findById(modelId);

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{modelId}")
    public ResponseEntity<ModelDto> updateModel(@PathVariable Integer modelId, @Valid @RequestBody ModelDto modelDto) {

        ModelDto updatedModel = modelService.update(modelId, modelDto);

        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<Void> deleteModelById(@PathVariable Integer modelId) {

        modelService.deleteById(modelId);

        return ResponseEntity.noContent().build();
    }
}
