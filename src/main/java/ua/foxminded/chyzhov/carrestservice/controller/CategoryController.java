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
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.service.CategoryService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "categories", description = "API for category management operations. Provides CRUD operations.")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get all categories with pagination",
            description = "Retrieve a paginated list of all categories"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully",
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
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @Parameter(description = "Pagination information") Pageable pageable) {

        Page<CategoryDto> categories = categoryService.findAll(pageable);

        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Create a new category",
            description = "Create a new category record. Requires authentification",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)
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
                    description = "Category already exists",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Parameter(description = "Category data to create", required = true)
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto savedCategory = categoryService.save(categoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Operation(
            summary = "Get category by Id",
            description = "Retrieve a specific category by its unique id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid categoryId format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with provided id",
                    content = @Content
            )
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @Parameter(description = "Unique identifier of the category", required = true, example = "1")
            @PathVariable Integer categoryId) {

        CategoryDto category = categoryService.findById(categoryId);

        return ResponseEntity.ok(category);
    }

    @Operation(
            summary = "Update category by Id",
            description = "Update an existing category. Requires authentification",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or categoryId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with provided id",
                    content = @Content
            )
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "Unique identifier of the category", required = true, example = "1")
            @PathVariable Integer categoryId,
            @Parameter(description = "Category data to update", required = true)
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto updatedCategory = categoryService.update(categoryId, categoryDto);

        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(
            summary = "Delete category by Id",
            description = "Delete a specific category. Requires authentification.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid categoryId format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with provided id",
                    content = @Content
            )
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(
            @Parameter(description = "Unique identifier of the category", required = true, example = "1")
            @PathVariable Integer categoryId) {

        categoryService.deleteById(categoryId);

        return ResponseEntity.noContent().build();
    }
}
