package ua.foxminded.chyzhov.carrestservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.service.CategoryService;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private CsvImportService csvImportService;

    @Test
    void getAllCategories_shouldReturnCategoriesPage_whenCategoriesExist() throws Exception {

        CategoryDto category = new CategoryDto(1, "Sedan");
        Page<CategoryDto> page = new PageImpl<>(List.of(category));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].category").value("Sedan"));
    }

    @Test
    void createCategory_shouldCreateCategory_whenAllDataProvided() throws Exception {

        CategoryDto category = new CategoryDto(2, "SUV");

        when(categoryService.save(any(CategoryDto.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categoryId": 2,
                                  "category": "SUV"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value("SUV"));
    }

    @Test
    void getCategoryById_shouldReturnCategoryById_whenCategoryWithIdExists() throws Exception {

        CategoryDto category = new CategoryDto(3, "Van");

        when(categoryService.findById(3)).thenReturn(category);

        mockMvc.perform(get("/api/v1/categories/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Van"));
    }

    @Test
    void updateCategory_shouldUpdateCategory_whenCategoryExistsAndAllDataProvided() throws Exception {

        CategoryDto category = new CategoryDto(1, "Bus");

        when(categoryService.update(any(Integer.class), any(CategoryDto.class))).thenReturn(category);

        mockMvc.perform(put("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "categoryId": 1,
                                  "category": "Bus"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Bus"));
    }

    @Test
    void deleteCategoryById_shouldDeleteCategory_whenCategoryWithIdExists() throws Exception {

        CategoryDto category = new CategoryDto(1, "Sedan");

        doNothing().when(categoryService).deleteById(1);

        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent());
    }
}
