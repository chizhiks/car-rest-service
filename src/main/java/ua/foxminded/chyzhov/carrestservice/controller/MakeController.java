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
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/makes")
@RequiredArgsConstructor
@Tag(name = "makes", description = "API for make management operations. Provides CRUD operations.")
public class MakeController {

    private final MakeService makeService;

    @Operation(
            summary = "Get all makes with pagination",
            description = "Retrieve a paginated list of all car makes"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Makes retrieved successfully",
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
    public ResponseEntity<Page<MakeDto>> getAllMakes(
            @Parameter(description = "Pagination information") Pageable pageable) {

        Page<MakeDto> makes = makeService.findAll(pageable);

        return ResponseEntity.ok(makes);
    }

    @Operation(
            summary = "Create a new make",
            description = "Create a new make record. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Make created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
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
                    description = "Make already exists",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<MakeDto> createMake(
            @Parameter(description = "Make data to create", required = true) @Valid @RequestBody MakeDto makeDto) {

        MakeDto savedMake = makeService.save(makeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMake);
    }

    @Operation(
            summary = "Get make by Id",
            description = "Retrieve a specific make by its unique id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Make retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid makeId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Make not found with provided id",
                    content = @Content
            )
    })
    @GetMapping("/{makeId}")
    public ResponseEntity<MakeDto> getMakeById(
            @Parameter(description = "Unique identifier of the make", required = true, example = "1")
            @PathVariable Integer makeId) {

        MakeDto make = makeService.findById(makeId);

        return ResponseEntity.ok(make);
    }

    @Operation(
            summary = "Get make by name",
            description = "Retrieve a specific make by its name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Make retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid makeName format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Make not found with provided name",
                    content = @Content
            )
    })
    @GetMapping("/name/{makeName}")
    public ResponseEntity<MakeDto> getMakeByName(
            @Parameter(description = "Name of the make", required = true, example = "Toyota")
            @PathVariable String makeName) {

        MakeDto make = makeService.findByName(makeName);

        return ResponseEntity.ok(make);
    }

    @Operation(
            summary = "Update make by Id",
            description = "Update an existing make. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Make updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MakeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or makeId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Make not found with provided id",
                    content = @Content
            )
    })
    @PutMapping("/{makeId}")
    public ResponseEntity<MakeDto> updateMake(
            @Parameter(description = "Unique identifier of the make", required = true, example = "1")
            @PathVariable Integer makeId,
            @Parameter(description = "Make data to update", required = true)
            @Valid @RequestBody MakeDto makeDto) {

        MakeDto updatedMake = makeService.update(makeId, makeDto);

        return ResponseEntity.ok(updatedMake);
    }

    @Operation(
            summary = "Delete make by Id",
            description = "Delete a specific make. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Make deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Make not found with provided id",
                    content = @Content
            )
    })
    @DeleteMapping("/{makeId}")
    public ResponseEntity<Void> deleteMake(
            @Parameter(description = "Unique identifier of the make", required = true, example = "1")
            @PathVariable Integer makeId) {

        makeService.deleteById(makeId);

        return ResponseEntity.noContent().build();
    }

}
