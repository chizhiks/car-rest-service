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

import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
@Tag(name = "models", description = "API for model management operations. Provides CRUD operations.")
public class ModelController {

    private final ModelService modelService;

    @Operation(
            summary = "Get all models with pagination",
            description = "Retrieve a paginated list of all models"
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
                    description = "Invalid pagination parameters",
                    content = @Content()
            )
    })
    @GetMapping
    public ResponseEntity<Page<ModelDto>> getAllModels(
            @Parameter(description = "Pagination information") Pageable pageable) {

        Page<ModelDto> models = modelService.findAll(pageable);

        return ResponseEntity.ok(models);
    }

    @Operation(
            summary = "Create a new model",
            description = "Create a new model record. Requires authentification.",
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
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Model already exists",
                    content = @Content()
            )
    })
    @PostMapping
    public ResponseEntity<ModelDto> createModel(
            @Parameter(description = "Model data to create", required = true)
            @Valid @RequestBody ModelDto modelDto) {

        ModelDto savedModel = modelService.save(modelDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedModel);
    }

    @Operation(
            summary = "Get model by id",
            description = "Retrieve a specific model by its unique id"
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
                    description = "invalid modelId format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Model not found with provided id",
                    content = @Content()
            )
    })
    @GetMapping("/{modelId}")
    public ResponseEntity<ModelDto> getModelById(
            @Parameter(description = "Unique identifier of the model", required = true, example = "1")
            @PathVariable Integer modelId) {

        ModelDto model = modelService.findById(modelId);

        return ResponseEntity.ok(model);
    }

    @Operation(
            summary = "Update model by id",
            description = "Update an existing model. Requires authentification",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Model updated successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or modelId format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Model not found with provided id",
                    content = @Content()
            )
    })
    @PutMapping("/{modelId}")
    public ResponseEntity<ModelDto> updateModel(
            @Parameter(description = "Unique identifier of the model", required = true, example = "1")
            @PathVariable Integer modelId,
            @Parameter(description = "Model data to update", required = true)
            @Valid @RequestBody ModelDto modelDto) {

        ModelDto updatedModel = modelService.update(modelId, modelDto);

        return ResponseEntity.ok(updatedModel);
    }

    @Operation(
            summary = "Delete model by id",
            description = "Delete a specific model. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Model deleted successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid modelId format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Model not found with provided id",
                    content = @Content()
            )
    })
    @DeleteMapping("/{modelId}")
    public ResponseEntity<Void> deleteModelById(
            @Parameter(description = "Unique identifier of the model", required = true, example = "1")
            @PathVariable Integer modelId) {

        modelService.deleteById(modelId);

        return ResponseEntity.noContent().build();
    }
}
