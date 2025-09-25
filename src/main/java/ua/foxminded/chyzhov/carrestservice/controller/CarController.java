package ua.foxminded.chyzhov.carrestservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "cars", description = "API for car management operations. Provides CRUD operations and filtering capabilities.")
public class CarController {

    private final CarService carService;

    @Operation(
            summary = "Get all cars with filtering and pagination",
            description = "Retrieve a paginated list of cars with optional filtering by make, model, year, etc."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cars retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<Page<CarDto>> getAllCars(
            @Parameter(description = "Filter criteria for cars") @ModelAttribute CarFilterDto carFilterDto,
            @Parameter(description = "Pagination information") Pageable pageable) {

        Page<CarDto> cars = carService.getFilteredCars(carFilterDto, pageable);

        return ResponseEntity.ok(cars);
    }

    @Operation(
            summary = "Create a new car",
            description = "Create a new car record. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Car created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Car already exists",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<CarDto> createCar(
            @Parameter(description = "Car data to create", required = true)
            @Valid @RequestBody CarDto carDto) {

        CarDto savedCar = carService.save(carDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @Operation(
            summary = "Get car by objectId",
            description = "Retrieve a specific car by its unique objectId"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Car found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid objectId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found with provided objectId",
                    content = @Content
            )
    })
    @GetMapping("/{objectId}")
    public ResponseEntity<CarDto> getCarByObjectId(
            @Parameter(description = "Unique identifier of the car", required = true, example = "ZRgPP9dBMm")
            @PathVariable String objectId) {

        CarDto car = carService.findByObjectId(objectId);

        return ResponseEntity.ok(car);
    }

    @Operation(
            summary = "Update car by objectId",
            description = "Update an existing car record. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Car updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or objectId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found with provided objectId",
                    content = @Content
            )
    })
    @PutMapping("/{objectId}")
    public ResponseEntity<CarDto> updateCar(
            @Parameter(description = "Unique identifier of the car", required = true, example = "ZRgPP9dBMm")
            @PathVariable String objectId,
            @Parameter(description = "Car data to update", required = true)
            @Valid @RequestBody CarDto carDto) {

        CarDto updatedCar = carService.update(objectId, carDto);

        return ResponseEntity.ok(updatedCar);
    }

    @Operation(
            summary = "Delete car by objectId",
            description = "Delete a specific car record. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Car deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid objectId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found with provided objectId",
                    content = @Content
            )
    })
    @DeleteMapping("/{objectId}")
    public ResponseEntity<Void> deleteCarByObjectId(
            @Parameter(description = "Unique identifier of the car", required = true, example = "ZRgPP9dBMm")
            @PathVariable String objectId) {

        carService.deleteByObjectId(objectId);

        return ResponseEntity.noContent().build();
    }

}
