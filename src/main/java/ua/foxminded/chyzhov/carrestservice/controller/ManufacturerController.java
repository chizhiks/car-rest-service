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
@Tag(name = "manufacturers", description = "API for manufacturer-centric operations. Provides hierarchical access to manufacturers, their models, and cars.")
public class ManufacturerController {

    private final MakeService makeService;
    private final ModelService modelService;
    private final CarService carService;

    @Operation(
            summary = "Get all manufacturers with pagination",
            description = "Retrieve a paginated list of all manufacturers"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content()
            )
    })
    @GetMapping
    public ResponseEntity<Page<MakeDto>> getAllManufacturers(
            @Parameter(description = "Pagination information") Pageable pageable) {

        Page<MakeDto> manufacturers = makeService.findAll(pageable);

        return ResponseEntity.ok(manufacturers);
    }

    @Operation(
            summary = "Create a new manufacturer",
            description = "Create a new manufacturer record. Requires authentification."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Manufacturer created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Manufacturer already exists",
                    content = @Content()
            )
    })
    @PostMapping
    public ResponseEntity<MakeDto> createManufacturer(
            @Parameter(description = "Manufacturer data to create", required = true)
            @Valid @RequestBody MakeDto makeDto) {

        MakeDto savedManufacturer = makeService.save(makeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @Operation(
            summary = "Get manufacturer by id",
            description = "Retrieve a specific manufacturer by its unique id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid makeId format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found with provided id",
                    content = @Content()
            )
    })
    @GetMapping("/id/{makeId}")
    public ResponseEntity<MakeDto> getManufacturerById(
            @Parameter(description = "Unique identifier of the manufacturer", required = true, example = "1")
            @PathVariable Integer makeId) {

        MakeDto manufacturer = makeService.findById(makeId);

        return ResponseEntity.ok(manufacturer);
    }

    @Operation(
            summary = "Get manufacturer by name",
            description = "Retrieve a specific manufacturer by its unique name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid manufacturerName format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found with provided name",
                    content = @Content()
            )
    })
    @GetMapping("/{manufacturerName}")
    public ResponseEntity<MakeDto> getManufacturerByName(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        return ResponseEntity.ok(manufacturer);
    }

    @Operation(
            summary = "Update manufacturer by name",
            description = "Update an existing manufacturer. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or manufacturerName format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Make not found with provided name",
                    content = @Content
            )
    })
    @PutMapping("/{manufacturerName}")
    public ResponseEntity<MakeDto> updateManufacturer(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Manufacturer data to update", required = true)
            @Valid @RequestBody MakeDto makeDto) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        MakeDto updatedManufacturer = makeService.update(manufacturer.makeId(), makeDto);

        return ResponseEntity.ok(updatedManufacturer);
    }

    @Operation(
            summary = "Delete manufacturer by name",
            description = "Delete a specific manufacturer. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Manufacturer deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found with provided name",
                    content = @Content
            )
    })
    @DeleteMapping("/{manufacturerName}")
    public ResponseEntity<Void> deleteManufacturer(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        makeService.deleteById(manufacturer.makeId());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get models by manufacturers",
            description = "Retrieve a paginated list of models by a specific manufacturer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Models retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid manufacturerName format or pagination parameters",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found with provided name",
                    content = @Content()
            )
    })
    @GetMapping("/{manufacturerName}/models")
    public ResponseEntity<Page<ModelDto>> getModelsByManufacturer(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Pagination information")
            Pageable pageable) {

        MakeDto manufacturer = makeService.findByName(manufacturerName);

        Page<ModelDto> models = modelService.findAllByMakeId(manufacturer.makeId(), pageable);

        return ResponseEntity.ok(models);
    }

    @Operation(
            summary = "Create model for manufacturer",
            description = "Create a new model record for a specific manufacturer. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Model created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class)
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
                    responseCode = "404",
                    description = "Manufacturer not found with provided name",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Model already exists",
                    content = @Content()
            )
    })
    @PostMapping("/{manufacturerName}/models")
    public ResponseEntity<ModelDto> createModelForManufacturer(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Name of the model to create", required = true, example = "Camry")
            @RequestBody String modelName) {

        ModelDto savedModel = modelService.saveByMakeName(manufacturerName, modelName);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedModel);
    }

    @Operation(
            summary = "Get model by manufacturer and model name",
            description = "Retrieve a specific model for a manufacturer by model name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Model retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer or model not found with provided name",
                    content = @Content()
            )
    })
    @GetMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<ModelDto> getModelByManufacturerAndName(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Name of the model", required = true, example = "Camry")
            @PathVariable String modelName) {

        ModelDto modelDto = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        return ResponseEntity.ok(modelDto);
    }

    @Operation(
            summary = "Create car by manufacturer, model for year",
            description = "Create a new car record for a specific manufacturer, model and year. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Car created successfully",
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
                    responseCode = "404",
                    description = "Manufacturer or model not found with provided name",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Car already exists",
                    content = @Content()
            )
    })
    @PostMapping("/{manufacturerName}/models/{modelName}/{year}")
    public ResponseEntity<CarDto> createCarByManufacturerModelYear(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Name of the model", required = true, example = "Camry")
            @PathVariable String modelName,
            @Parameter(description = "Year of the car", required = true, example = "2025")
            @PathVariable Integer year,
            @Parameter(description = "ObjectId of the car", required = true, example = "ZRgPP9dBMm")
            @RequestBody String objectId) {

        CarDto savedCar = carService.saveByMakeModelYear(objectId, manufacturerName, modelName, year);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @Operation(
            summary = "Update model by manufacturer and model name",
            description = "Update an existing model by a manufacturer. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Model updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or parameter format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer or model not found with provided name",
                    content = @Content
            )
    })
    @PutMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<ModelDto> updateModelByManufacturerAndName(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Name of the model", required = true, example = "Camry")
            @PathVariable String modelName,
            @Parameter(description = "Model data to update", required = true)
            @Valid @RequestBody ModelDto modelDto) {

        ModelDto model = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        ModelDto updatedModel = modelService.update(model.modelId(), modelDto);

        return ResponseEntity.ok(updatedModel);
    }

    @Operation(
            summary = "Delete model by manufacturer and model name",
            description = "Delete a specific model by a manufacturer. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Model deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer or model not found with provided name",
                    content = @Content
            )
    })
    @DeleteMapping("/{manufacturerName}/models/{modelName}")
    public ResponseEntity<Void> deleteModelByManufacturerAndName(
            @Parameter(description = "Name of the manufacturer", required = true, example = "Toyota")
            @PathVariable String manufacturerName,
            @Parameter(description = "Name of the model", required = true, example = "Camry")
            @PathVariable String modelName) {

        ModelDto model = modelService.findByMakeNameAndModelName(manufacturerName, modelName);

        modelService.deleteById(model.modelId());
        return ResponseEntity.noContent().build();
    }
}
