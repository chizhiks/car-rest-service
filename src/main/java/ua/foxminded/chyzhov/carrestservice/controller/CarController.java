package ua.foxminded.chyzhov.carrestservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CarFilterDto;
import ua.foxminded.chyzhov.carrestservice.service.CarService;

import org.springframework.data.domain.Pageable;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<Page<CarDto>> getAllCars(@ModelAttribute CarFilterDto carFilterDto, Pageable pageable) {

        Page<CarDto> cars = carService.getFilteredCars(carFilterDto, pageable);

        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CarDto carDto) {

        CarDto savedCar = carService.save(carDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @GetMapping("/{objectId}")
    public ResponseEntity<CarDto> getCarByObjectId(@PathVariable String objectId) {

        CarDto car = carService.findByObjectId(objectId);

        return ResponseEntity.ok(car);
    }

    @PutMapping("/{objectId}")
    public ResponseEntity<CarDto> updateCar(@PathVariable String objectId, @Valid @RequestBody CarDto carDto) {

        CarDto updatedCar = carService.update(objectId, carDto);

        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{objectId}")
    public ResponseEntity<Void> deleteCarByObjectId(@PathVariable String objectId) {

        carService.deleteByObjectId(objectId);

        return ResponseEntity.noContent().build();
    }

}
