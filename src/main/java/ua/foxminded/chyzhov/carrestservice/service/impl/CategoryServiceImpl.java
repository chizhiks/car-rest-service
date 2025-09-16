package ua.foxminded.chyzhov.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.mapper.CategoryMapper;
import ua.foxminded.chyzhov.carrestservice.repository.CategoryRepository;
import ua.foxminded.chyzhov.carrestservice.service.CategoryService;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.NotFoundException;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.RecordAlreadyExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto findById(Integer categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category", categoryId));

        log.info("Category with categoryId: {} found successfully", category);

        return categoryMapper.toDto(category);
    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        log.info("{} categories have been retrieved successfully", categoryPage.getTotalElements());

        return categoryPage.map(categoryMapper::toDto);
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {

        if (categoryRepository.existsByCategory(categoryDto.category())) {
            log.error("Category with categoryName: {} already exists", categoryDto.category());
            throw new RecordAlreadyExists("Category", "categoryName", categoryDto.category());
        }

        Category category = categoryMapper.toEntity(categoryDto);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category with categoryId: {} saved successfully", savedCategory);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(Integer categoryId, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category", categoryId));

        category.setCategory(categoryDto.category());

        Category updatedCategory = categoryRepository.save(category);

        log.info("Category with categoryId: {} updated successfully", updatedCategory);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteById(Integer categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            log.info("Category with categoryId: {} deleted successfully", categoryId);
        } catch (Exception e) {
            log.error("Error deleting category with categoryId: {}", categoryId, e);
            throw new NotFoundException("Category", categoryId);
        }
    }
}
