package ua.foxminded.chyzhov.carrestservice.repository;

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
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;

import jakarta.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class CarRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        Make make = new Make();
        make.setMake("Toyota");
        entityManager.persist(make);
        entityManager.flush();

        Model model = new Model();
        model.setModel("Camry");
        model.setMake(make);
        entityManager.persist(model);
        entityManager.flush();

        Car car = new Car();
        car.setObjectId("ToyotaCamry2022");
        car.setModel(model);
        car.setYear(2022);
        entityManager.persist(car);
        entityManager.flush();
    }

    @Test
    public void findByObjectId_shouldReturnCar_whenCarWithObjectIdExists() {
        Optional<Car> result = carRepository.findByObjectId("ToyotaCamry2022");

        assertThat(result).isPresent();
        Car car = result.get();
        assertThat(car.getObjectId()).isEqualTo("ToyotaCamry2022");
        assertThat(car.getYear()).isEqualTo(2022);
        assertThat(car.getModel().getModel()).isEqualTo("Camry");
        assertThat(car.getModel().getMake().getMake()).isEqualTo("Toyota");
    }

    @Test
    public void findByObjectId_shouldReturnEmptyOptional_whenCarWithObjectIdDoesNotExists() {
        Optional<Car> result = carRepository.findByObjectId("Rolls-Royce");

        assertThat(result).isEmpty();
    }

    @Test
    public void existsByObjectId_shouldReturnTrue_whenCarWithObjectIdExists() {
        boolean result = carRepository.existsByObjectId("ToyotaCamry2022");

        assertThat(result).isTrue();
    }

    @Test
    public void existsByObjectId_shouldReturnFalse_whenCarWithObjectIdDoesNotExists() {
        boolean result = carRepository.existsByObjectId("Rolls-RoyceCullinan2024");

        assertThat(result).isFalse();
    }

    @Test
    public void deleteByObjectId_shouldDeleteCar_whenCarWithObjectIdExists() {
        carRepository.deleteByObjectId("ToyotaCamry2022");

        boolean result = carRepository.existsByObjectId("ToyotaCamry2022");

        assertThat(result).isFalse();
    }
}