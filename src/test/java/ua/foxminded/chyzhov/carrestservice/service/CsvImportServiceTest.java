package ua.foxminded.chyzhov.carrestservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.repository.CarRepository;
import ua.foxminded.chyzhov.carrestservice.repository.CategoryRepository;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CsvImportServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CsvImportService csvImportService;

    @TempDir
    private Path tempDir;

    private Make toyota;
    private Make bmw;

    private MakeDto toyotaDto;
    private MakeDto bmwDto;

    private Model camry;
    private Model x5;

    private ModelDto camryDto;
    private ModelDto x5Dto;

    private Category sedan;
    private Category suv;

    private CategoryDto sedanDto;
    private CategoryDto suvDto;

    private Car toyotaCamry;
    private Car bmwX5;

    private CarDto toyotaCamryDto;
    private CarDto bmwX5Dto;

    @BeforeEach
    void setUp() {

        toyota = new Make(1, "Toyota");
        bmw = new Make(2, "BMW");

        toyotaDto = new MakeDto(1, "Toyota");
        bmwDto = new MakeDto(2, "BMW");

        camry = new Model(1, toyota, "Camry");
        x5 = new Model(2, bmw, "X5");

        camryDto = new ModelDto(1, toyotaDto, "Camry");
        x5Dto = new ModelDto(2, bmwDto, "X5");

        sedan = new Category(1, "Sedan");
        suv = new Category(2, "SUV");

        sedanDto = new CategoryDto(1, "Sedan");
        suvDto = new CategoryDto(2, "SUV");

        toyotaCamry = new Car("toyotaCamry2022", camry, 2022, Set.of(sedan));
        bmwX5 = new Car("bmwX52016", x5, 2016, Set.of(suv));

        toyotaCamryDto = new CarDto("toyotaCamry2022", camryDto, 2022, Set.of(sedanDto));
        bmwX5Dto = new CarDto("bmwX52016", x5Dto, 2016, Set.of(suvDto));
    }

    @Test
    public void importCarsFromCsv_shouldProcessNewCar_whenFileAndMakeAndModelExist() throws IOException {

        String csvContent = """
                objectId,make,model,year,category
                toyotaCamry2022,Toyota,Camry,2020,Sedan
                """;

        Path csvFile = tempDir.resolve("test_cars.csv");
        Files.writeString(csvFile, csvContent);

        when(makeRepository.findByMakeIgnoreCase("Toyota")).thenReturn(Optional.of(toyota));
        when(modelRepository.findByModelIgnoreCaseAndMake("Camry", toyota)).thenReturn(Optional.of(camry));
        when(categoryRepository.findByCategory("Sedan")).thenReturn(Optional.of(sedan));
        when(carRepository.findByObjectId("toyotaCamry2022")).thenReturn(Optional.empty());
        when(carRepository.save(any(Car.class))).thenReturn(toyotaCamry);

        csvImportService.importCarsFromCsv(csvFile.toString());

        verify(makeRepository).findByMakeIgnoreCase("Toyota");
        verify(modelRepository).findByModelIgnoreCaseAndMake("Camry", toyota);
        verify(categoryRepository).findByCategory("Sedan");
        verify(carRepository).findByObjectId("toyotaCamry2022");
        verify(carRepository).save(any(Car.class));

        verify(makeRepository, never()).save(any(Make.class));
        verify(modelRepository, never()).save(any(Model.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void importCarsFromCsv_shouldCreateNewMakeAndModel_whenMakeAndModelNotExist() throws IOException {

        String csvContent = """
                        objectId,make,model,year,category
                        bmwX52016,BMW,X5,2016,SUV
                """;

        Path csvFile = tempDir.resolve("test_cars_new.csv");
        Files.writeString(csvFile, csvContent);

        when(makeRepository.findByMakeIgnoreCase("BMW")).thenReturn(Optional.empty());
        when(makeRepository.save(any(Make.class))).thenReturn(bmw);

        when(modelRepository.findByModelIgnoreCaseAndMake("X5", bmw)).thenReturn(Optional.empty());
        when(modelRepository.save(any(Model.class))).thenReturn(x5);

        when(categoryRepository.findByCategory("SUV")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(suv);

        when(carRepository.findByObjectId("bmwX52016")).thenReturn(Optional.empty());
        when(carRepository.save(any(Car.class))).thenReturn(bmwX5);

        csvImportService.importCarsFromCsv(csvFile.toString());

        verify(makeRepository).save(argThat(make ->
                make.getMake().equals("BMW")
        ));

        verify(modelRepository).save(argThat(model ->
                model.getMake().equals(bmw) && model.getModel().equals("X5")
        ));

        verify(categoryRepository).save(argThat(category ->
                category.getCategory().equals("SUV")));

        verify(carRepository).save(any(Car.class));
    }

    @Test
    public void importCarsFromCsv_shouldHandleEmptyCategories_whenCategoriesNotProvided() throws IOException {

        String csvContent = """
                        objectId,make,model,year,category
                        no_category_bmwX52016,BMW,X5,2016,
        """;

        Path csvFile = tempDir.resolve("test_no_categories.csv");
        Files.writeString(csvFile, csvContent);

        when(makeRepository.findByMakeIgnoreCase("BMW")).thenReturn(Optional.of(bmw));
        when(modelRepository.findByModelIgnoreCaseAndMake("X5", bmw)).thenReturn(Optional.of(x5));
        when(carRepository.findByObjectId("no_category_bmwX52016")).thenReturn(Optional.empty());
        when(carRepository.save(any(Car.class))).thenReturn(bmwX5);

        csvImportService.importCarsFromCsv(csvFile.toString());

        verify(carRepository).save(argThat(car ->
                car.getObjectId().equals("no_category_bmwX52016") &&
                car.getCategories().isEmpty()));

        verify(categoryRepository, never()).findByCategory(anyString());
    }
}
