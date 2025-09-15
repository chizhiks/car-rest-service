package ua.foxminded.chyzhov.carrestservice.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.chyzhov.carrestservice.config.TestConfig;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class ModelRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
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
    private ModelRepository modelRepository;

    @Autowired
    private EntityManager entityManager;

    private Make toyota;

    @BeforeEach
    public void setup() {
        Make bmw = new Make();
        bmw.setMake("BMW");
        entityManager.persist(bmw);
        entityManager.flush();

        toyota = new Make();
        toyota.setMake("Toyota");
        entityManager.persist(toyota);
        entityManager.flush();

        Model x5 =  new Model();
        x5.setMake(bmw);
        x5.setModel("X5");
        entityManager.persist(x5);
        entityManager.flush();

        Model camry = new Model();
        camry.setMake(toyota);
        camry.setModel("Camry");
        entityManager.persist(camry);
        entityManager.flush();

        Model prado = new Model();
        prado.setMake(toyota);
        prado.setModel("Prado");
        entityManager.persist(prado);
        entityManager.flush();
    }

    @Test
    public void existsByModelIgnoreCaseAndMakeMakeIgnoreCase_shouldReturnTrue_whenModelWithMakeAndModelNameExists() {
        boolean modelExists = modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase("X5", "BMW");

        assertThat(modelExists).isTrue();
    }

    @Test
    public void existsByModelIgnoreCaseAndMakeMakeIgnoreCase_shouldReturnFalse_whenModelWithMakeAndModelNameDoesNotExist() {
        boolean modelExists = modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase("ES350", "Lexus");

        assertThat(modelExists).isFalse();
    }

    @Test
    public void findByModelIgnoreCaseAndMake_shouldReturnModel_whenModelWithMakeAndModelNameExists() {
        Optional<Model> result = modelRepository.findByModelIgnoreCaseAndMake("Camry", toyota);

        assertThat(result.isPresent()).isTrue();
        Model model = result.get();
        assertThat(model.getMake().getMake()).isEqualTo("Toyota");
        assertThat(model.getModel()).isEqualTo("Camry");
    }

    @Test
    public void findByModelIgnoreCaseAndMake_shouldReturnEmptyOptional_whenModelWithMakeAndModelNameDoesNotExist() {
        Optional<Model> result = modelRepository.findByModelIgnoreCaseAndMake("Land Cruiser 200", toyota);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findByModelIgnoreCaseAndMakeMakeIgnoreCase_shouldReturnModel_whenModelWithMakeNameAndModelNameExists() {
        Optional<Model> result = modelRepository.findByModelIgnoreCaseAndMakeMakeIgnoreCase("X5", "BMW");

        assertThat(result.isPresent()).isTrue();
        Model model = result.get();
        assertThat(model.getMake().getMake()).isEqualTo("BMW");
        assertThat(model.getModel()).isEqualTo("X5");
    }

    @Test
    public void findByModelIgnoreCaseAndMakeMakeIgnoreCase_shouldReturnModel_whenModelWithMakeNameAndModelNameDoesNotExist() {
        Optional<Model> result = modelRepository.findByModelIgnoreCaseAndMakeMakeIgnoreCase("M5F90", "BMW");

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findAllByMakeMakeId_shouldReturnPageOfModels_whenModelsWithMakeIdExist() {
        Pageable pageable = Pageable.unpaged();

        Page<Model> result = modelRepository.findAllByMakeMakeId(toyota.getMakeId(), pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);

        Model camry = result.getContent().get(0);
        assertThat(camry.getMake().getMake()).isEqualTo("Toyota");
        assertThat(camry.getModel()).isEqualTo("Camry");

        Model prado = result.getContent().get(1);
        assertThat(prado.getMake().getMake()).isEqualTo("Toyota");
        assertThat(prado.getModel()).isEqualTo("Prado");
    }
}
