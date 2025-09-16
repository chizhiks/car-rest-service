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
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.service.CarService;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final MakeService makeService;
    private final ModelService modelService;
    private final CarService carService;

    @GetMapping
    public ResponseEntity<Page<MakeDto>> getAllManufacturers(Pageable pageable) {

        Page<MakeDto> manufacturers = makeService.findAll(pageable);

        return ResponseEntity.ok(manufacturers);
    }

    @PostMapping
    public ResponseEntity<MakeDto> createManufacturer(@Valid @RequestBody MakeDto makeDto) {

        MakeDto savedManufacturer = makeService.save(makeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @GetMapping("/id/{makeId}")
    public ResponseEntity<MakeDto> getManufacturerById(@PathVariable Integer makeId) {

        MakeDto manufacturer = makeService.findById(makeId);

        return ResponseEntity.ok(manufacturer);
    }

    @GetMapping("/{manufacturerName}")
    public ResponseEntity<MakeDto> getManufacturerByName(@PathVariable String manufacturerName) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        return ResponseEntity.ok(manufacturer);
    }

    @PutMapping("/{manufacturerName}")
    public ResponseEntity<MakeDto> updateManufacturer(@PathVariable String manufacturerName, @Valid @RequestBody MakeDto makeDto) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        MakeDto updatedManufacturer = makeService.update(manufacturer.makeId(), makeDto);

        return ResponseEntity.ok(updatedManufacturer);
    }

    @DeleteMapping("/{manufacturerName}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable String manufacturerName) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        makeService.deleteById(manufacturer.makeId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{manufacturerName}/models")
    public ResponseEntity<Page<ModelDto>> getModelsByManufacturer(@PathVariable String manufacturerName, Pageable pageable) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        Page<ModelDto> models = modelService.findAllByMakeId(manufacturer.makeId(), pageable);

        return ResponseEntity.ok(models);
    }

    @PostMapping("/{manufacturerName}/models")
    public ResponseEntity<ModelDto> createModelForManufacturer(@PathVariable String manufacturerName, @RequestBody String modelName) {

        ModelDto savedModel = modelService.saveByMakeName(manufacturerName, modelName);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedModel);
    }

    @GetMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<ModelDto> getModelByManufacturerAndName(@PathVariable String manufacturerName, @PathVariable String modelName) {

        ModelDto modelDto = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        return ResponseEntity.ok(modelDto);
    }

    @PostMapping("/{manufacturerName}/models/{modelName}/{year}")
    public ResponseEntity<CarDto> createCarByManufacturerModelYear(@PathVariable String manufacturerName, @PathVariable String modelName, @PathVariable Integer year, @RequestBody String objectId) {

        CarDto savedCar = carService.saveByMakeModelYear(objectId, manufacturerName, modelName, year);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @PutMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<ModelDto> updateModelByManufacturerAndName(@PathVariable String manufacturerName, @PathVariable String modelName, @Valid @RequestBody ModelDto modelDto) {

        ModelDto model = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        ModelDto updatedModel = modelService.update(model.modelId(), modelDto);

        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<Void> deleteModelByManufacturerAndName(@PathVariable String manufacturerName, @PathVariable String modelName) {

        ModelDto model = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        modelService.deleteById(model.modelId());
        return ResponseEntity.noContent().build();
    }
}
