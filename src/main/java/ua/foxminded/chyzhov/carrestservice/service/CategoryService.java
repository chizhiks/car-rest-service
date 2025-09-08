package ua.foxminded.chyzhov.carrestservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;

public interface CategoryService {

    CategoryDto findById(Integer categoryId);

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Integer categoryId, CategoryDto categoryDto);

    void deleteById(Integer categoryId);
}
