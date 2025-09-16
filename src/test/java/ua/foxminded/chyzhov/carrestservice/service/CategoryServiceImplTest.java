package ua.foxminded.chyzhov.carrestservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.mapper.CategoryMapper;
import ua.foxminded.chyzhov.carrestservice.repository.CategoryRepository;
import ua.foxminded.chyzhov.carrestservice.service.impl.CategoryServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category sedan;
    Category suv;
    Category crossover;

    CategoryDto sedanDto;
    CategoryDto suvDto;
    CategoryDto crossoverDto;

    @BeforeEach
    public void setUp() {

        sedan = new Category(1, "Sedan");
        suv = new Category(2, "SUV");
        crossover = new Category(3, "Crossover");

        sedanDto = new CategoryDto(1, "Sedan");
        suvDto = new CategoryDto(2, "SUV");
        crossoverDto = new CategoryDto(3, "Crossover");
    }

    @Test
    public void findById_shouldReturnCategoryDto_whenCategoryWithIdExists() {

        when(categoryRepository.findById(1)).thenReturn(Optional.of(sedan));
        when(categoryMapper.toDto(sedan)).thenReturn(sedanDto);

        CategoryDto result = categoryService.findById(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(sedanDto);

        verify(categoryRepository).findById(1);
        verify(categoryMapper).toDto(sedan);
    }

    @Test
    public void findAll_shouldReturnPageOfCategoryDto_whenCategoriesExist() {

        Pageable pageable = Pageable.unpaged();

        Page<Category> categoryPage = new PageImpl<>(Arrays.asList(sedan, suv, crossover));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(sedan)).thenReturn(sedanDto);
        when(categoryMapper.toDto(suv)).thenReturn(suvDto);
        when(categoryMapper.toDto(crossover)).thenReturn(crossoverDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).contains(sedanDto, suvDto, crossoverDto);

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(sedan);
        verify(categoryMapper).toDto(suv);
        verify(categoryMapper).toDto(crossover);
    }

    @Test
    public void save_shouldSaveAndReturnCategoryDto_whenAllDataProvided() {

        when(categoryRepository.existsByCategory(suvDto.category())).thenReturn(false);
        when(categoryMapper.toEntity(suvDto)).thenReturn(suv);
        when(categoryRepository.save(suv)).thenReturn(suv);
        when(categoryMapper.toDto(suv)).thenReturn(suvDto);

        CategoryDto result = categoryService.save(suvDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(suvDto);

        verify(categoryRepository).existsByCategory(suvDto.category());
        verify(categoryMapper).toEntity(suvDto);
        verify(categoryMapper).toDto(suv);
        verify(categoryRepository).save(suv);
    }

    @Test
    public void update_shouldUpdateAndReturnCategoryDto_whenAllDataProvided() {

        when(categoryRepository.findById(3)).thenReturn(Optional.of(crossover));
        when(categoryRepository.save(crossover)).thenReturn(crossover);
        when(categoryMapper.toDto(crossover)).thenReturn(crossoverDto);

        CategoryDto result = categoryService.update(3, crossoverDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(crossoverDto);

        verify(categoryRepository).findById(3);
        verify(categoryRepository).save(crossover);
        verify(categoryMapper).toDto(crossover);
    }

    @Test
    public void deleteById_shouldDeleteCategory_whenCategoryExists() {

        categoryService.deleteById(3);

        verify(categoryRepository).deleteById(3);
    }
}
