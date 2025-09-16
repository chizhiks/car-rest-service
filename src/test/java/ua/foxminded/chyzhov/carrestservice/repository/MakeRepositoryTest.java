package ua.foxminded.chyzhov.carrestservice.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
import ua.foxminded.chyzhov.carrestservice.entity.Make;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class MakeRepositoryTest {

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
    private MakeRepository makeRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        Make bmw = new Make();
        bmw.setMake("BMW");
        entityManager.persist(bmw);
        entityManager.flush();

        Make toyota = new Make();
        toyota.setMake("Toyota");
        entityManager.persist(toyota);
        entityManager.flush();
    }

    @Test
    public void existsByMake_shouldReturnTrue_whenMakeWithMakeNameExists() {
        boolean existsByMake = makeRepository.existsByMake("BMW");

        assertThat(existsByMake).isTrue();
    }

    @Test
    public void existsByMake_shouldReturnFalse_whenMakeWithMakeNameDoesNotExist() {
        boolean existsByMake = makeRepository.existsByMake("Lexus");

        assertThat(existsByMake).isFalse();
    }

    @Test
    public void findByMakeIgnoreCase_shouldReturnMake_whenMakeWithMakeNameExists() {
        Optional<Make> result = makeRepository.findByMakeIgnoreCase("Toyota");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getMake()).isEqualTo("Toyota");
    }

    @Test
    public void findByMakeIgnoreCase_shouldReturnEmptyOptional_whenMakeWithMakeNameDoesNotExist() {
        Optional<Make> result = makeRepository.findByMakeIgnoreCase("Mercedes-Benz");

        assertThat(result.isPresent()).isFalse();
    }
}
