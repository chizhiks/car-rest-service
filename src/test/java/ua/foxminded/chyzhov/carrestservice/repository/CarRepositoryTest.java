package ua.foxminded.chyzhov.carrestservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @MockitoBean
    private CsvImportService csvImportService;

    private Car car;
    private Make make;
    private Model model;

    @BeforeEach
    public void setup() {
        make = new Make();
        make.setMake("Toyota");
        entityManager.persist(make);

        model = new Model();
        model.setModel("Camry");
        model.setMake(make);
        entityManager.persist(model);

        car = new Car();
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
    }
}