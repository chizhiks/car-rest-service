package ua.foxminded.chyzhov.carrestservice.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.chyzhov.carrestservice.config.TestConfig;
import ua.foxminded.chyzhov.carrestservice.entity.Category;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class CategoryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =  new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        Category sedan = new Category();
        sedan.setCategory("sedan");
        entityManager.persist(sedan);
        entityManager.flush();

        Category suv = new Category();
        suv.setCategory("SUV");
        entityManager.persist(suv);
        entityManager.flush();
    }

    @Test
    public void existsByCategory_shouldReturnTrue_whenCategoryWithCategoryNameExists() {
        boolean existsByCategory = categoryRepository.existsByCategory("sedan");

        assertThat(existsByCategory).isTrue();
    }

    @Test
    public void existsByCategory_shouldReturnFalse_whenCategoryWithCategoryNameDoesNotExist() {
        boolean existsByCategory = categoryRepository.existsByCategory("van");

        assertThat(existsByCategory).isFalse();
    }

    @Test
    public void findByCategory_shouldReturnCategory_whenCategoryWithCategoryNameExists() {
        Optional<Category> result = categoryRepository.findByCategory("SUV");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getCategory()).isEqualTo("SUV");
    }

    @Test
    public void findByCategory_shouldReturnEmptyOptional_whenCategoryWithCategoryNameDoesNotExist() {
        Optional<Category> result = categoryRepository.findByCategory("van");

        assertThat(result.isPresent()).isFalse();
    }
}
