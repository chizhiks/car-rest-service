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
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.chyzhov.carrestservice.dto.*;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.mapper.CarMapper;
import ua.foxminded.chyzhov.carrestservice.mapper.CategoryMapper;
import ua.foxminded.chyzhov.carrestservice.mapper.ModelMapper;
import ua.foxminded.chyzhov.carrestservice.repository.CarRepository;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;
import ua.foxminded.chyzhov.carrestservice.service.impl.CarServiceImpl;
import ua.foxminded.chyzhov.carrestservice.specification.CarSpecificationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CarSpecificationBuilder carSpecificationBuilder;

    @InjectMocks
    private CarServiceImpl carService;

    private Make toyota;
    private MakeDto toyotaDto;
    private Make volkswagen;
    private MakeDto volkswagenDto;
    private Make kia;
    private MakeDto kiaDto;

    private Model camry;
    private ModelDto camryDto;
    private Model golf;
    private ModelDto golfDto;
    private Model tiguan;
    private ModelDto tiguanDto;
    private Model sportage;
    private ModelDto sportageDto;

    private Category sedan;
    private CategoryDto sedanDto;
    private Category hatchback;
    private CategoryDto hatchbackDto;
    private Category crossover;
    private CategoryDto crossoverDto;

    private Car toyotaCamry2006;
    private CarDto toyotaCamry2006Dto;
    private Car toyotaCamry2022;
    private CarDto toyotaCamry2022Dto;
    private Car volkswagenGolf2017;
    private CarDto volkswagenGolf2017Dto;
    private Car volkswagenTiguan2022;
    private CarDto volkswagenTiguan2022Dto;
    private Car kiaSportage2014;
    private CarDto kiaSportage2014Dto;

    @BeforeEach
    public void setUp() {

        toyota = new Make(1, "Toyota");
        volkswagen = new Make(2, "Volkswagen");
        kia = new Make(3, "Kia");

        toyotaDto = new MakeDto(1, "Toyota");
        volkswagenDto = new MakeDto(2, "Volkswagen");
        kiaDto = new MakeDto(3, "Kia");

        camry = new Model(1, toyota, "Camry");
        golf = new Model(2, volkswagen, "Golf");
        tiguan = new Model(3, volkswagen, "Tiguan");
        sportage = new Model(4, kia, "Sportage");

        camryDto = new ModelDto(1, toyotaDto, "Camry");
        golfDto = new ModelDto(2, volkswagenDto, "Golf");
        tiguanDto = new ModelDto(3, volkswagenDto, "Tiguan");
        sportageDto = new ModelDto(4, kiaDto, "Sportage");

        sedan = new Category(1, "Sedan");
        hatchback = new Category(2, "Hatchback");
        crossover = new Category(3, "Crossover");

        sedanDto = new CategoryDto(1, "Sedan");
        hatchbackDto = new CategoryDto(2, "Hatchback");
        crossoverDto = new CategoryDto(3, "Crossover");

        toyotaCamry2006 = new Car("toyotaCamry2006", camry, 2006, Set.of(sedan));
        toyotaCamry2022 = new Car("toyotaCamry2022", camry, 2022, Set.of(sedan));
        volkswagenGolf2017 = new Car("volkswagenGolf2017", golf, 2017, Set.of(hatchback));
        volkswagenTiguan2022 = new Car("volkswagenTiguan2022", tiguan, 2022, Set.of(crossover));
        kiaSportage2014 = new Car("kiaSportage2014", sportage, 2014, Set.of(crossover));

        toyotaCamry2006Dto = new CarDto("toyotaCamry2006", camryDto, 2006, Set.of(sedanDto));
        toyotaCamry2022Dto = new CarDto("toyotaCamry2022", camryDto, 2022, Set.of(sedanDto));
        volkswagenGolf2017Dto = new CarDto("volkswagenGolf2017", golfDto, 2017, Set.of(hatchbackDto));
        volkswagenTiguan2022Dto = new CarDto("volkswagenTiguan2022", tiguanDto, 2022, Set.of(sedanDto));
        kiaSportage2014Dto = new CarDto("kiaSportage2014", sportageDto, 2014, Set.of(hatchbackDto));
    }

    @Test
    public void getFilteredCars_shouldReturnPage_whenFilterIsValid() {

        CarFilterDto filterDto = new CarFilterDto("Toyota", null, 2000, 2023, null);

        Pageable pageable = Pageable.unpaged();

        MakeDto toyotaDto = new MakeDto(1, "Toyota");
        ModelDto camryDto = new ModelDto(1, toyotaDto, "Camry");
        CategoryDto sedanDto = new CategoryDto(1, "Sedan");

        CarDto toyotaCamry2006Dto = new CarDto("toyotaCamry2006", camryDto, 2006, Set.of(sedanDto));
        CarDto toyotaCamry2022Dto = new CarDto("toyotaCamry2022", camryDto, 2022, Set.of(sedanDto));

        Page<Car> carPage = new PageImpl<>(List.of(toyotaCamry2006, toyotaCamry2022));
        Specification<Car> specification = mock(Specification.class);

        when(carSpecificationBuilder.build(filterDto)).thenReturn(specification);
        when(carRepository.findAll(specification, pageable)).thenReturn(carPage);
        when(carMapper.toDto(toyotaCamry2006)).thenReturn(toyotaCamry2006Dto);
        when(carMapper.toDto(toyotaCamry2022)).thenReturn(toyotaCamry2022Dto);

        Page<CarDto> result = carService.getFilteredCars(filterDto, pageable);

        assertThat(result).hasSize(2);
        assertThat(result.getContent()).contains(toyotaCamry2006Dto, toyotaCamry2022Dto);
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(carSpecificationBuilder).build(filterDto);
        verify(carRepository).findAll(specification, pageable);
        verify(carMapper).toDto(toyotaCamry2006);
        verify(carMapper).toDto(toyotaCamry2022);
    }

    @Test
    public void findByObjectId_shouldReturnCarDto_whenCarWithObjectIdExists() {

        when(carRepository.findByObjectId("volkswagenGolf2017")).thenReturn(Optional.of(volkswagenGolf2017));
        when(carMapper.toDto(volkswagenGolf2017)).thenReturn(volkswagenGolf2017Dto);

        CarDto result = carService.findByObjectId("volkswagenGolf2017");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(volkswagenGolf2017Dto);

        verify(carRepository).findByObjectId("volkswagenGolf2017");
        verify(carMapper).toDto(volkswagenGolf2017);
    }

    @Test
    public void findAll_shouldReturnListOfCarDto_whenCarsExist() {

        when(carRepository.findAll()).thenReturn(List.of(toyotaCamry2006, toyotaCamry2022, volkswagenGolf2017, volkswagenTiguan2022, kiaSportage2014));
        when(carMapper.toDto(toyotaCamry2006)).thenReturn(toyotaCamry2006Dto);
        when(carMapper.toDto(toyotaCamry2022)).thenReturn(toyotaCamry2022Dto);
        when(carMapper.toDto(volkswagenGolf2017)).thenReturn(volkswagenGolf2017Dto);
        when(carMapper.toDto(volkswagenTiguan2022)).thenReturn(volkswagenTiguan2022Dto);
        when(carMapper.toDto(kiaSportage2014)).thenReturn(kiaSportage2014Dto);

        List<CarDto> result = carService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(5);
        assertThat(result).contains(toyotaCamry2006Dto, toyotaCamry2022Dto, volkswagenGolf2017Dto, volkswagenTiguan2022Dto, kiaSportage2014Dto);

        verify(carRepository).findAll();
        verify(carMapper).toDto(toyotaCamry2006);
        verify(carMapper).toDto(toyotaCamry2022);
        verify(carMapper).toDto(volkswagenGolf2017);
        verify(carMapper).toDto(volkswagenTiguan2022);
        verify(carMapper).toDto(kiaSportage2014);
    }

    @Test
    public void save_shouldSaveAndReturnCarDto_whenAllDataProvided() {

        when(carRepository.existsByObjectId("volkswagenTiguan2022")).thenReturn(Boolean.FALSE);
        when(carMapper.toEntity(volkswagenTiguan2022Dto)).thenReturn(volkswagenTiguan2022);
        when(carRepository.save(volkswagenTiguan2022)).thenReturn(volkswagenTiguan2022);
        when(carMapper.toDto(volkswagenTiguan2022)).thenReturn(volkswagenTiguan2022Dto);

        CarDto result = carService.save(volkswagenTiguan2022Dto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(volkswagenTiguan2022Dto);

        verify(carRepository).existsByObjectId("volkswagenTiguan2022");
        verify(carMapper).toEntity(volkswagenTiguan2022Dto);
        verify(carRepository).save(volkswagenTiguan2022);
        verify(carMapper).toDto(volkswagenTiguan2022);
    }

    @Test
    public void saveByMakeModelYear_shouldSaveAndReturnCarDto_whenAllDataProvided() {

        when(carRepository.existsByObjectId("kiaSportage2014")).thenReturn(Boolean.FALSE);
        when(makeRepository.findByMakeIgnoreCase("Kia")).thenReturn(Optional.of(kia));
        when(modelRepository.findByModelIgnoreCaseAndMake("Sportage", kia)).thenReturn(Optional.of(sportage));
        when(carRepository.save(any(Car.class))).thenReturn(kiaSportage2014);
        when(carMapper.toDto(kiaSportage2014)).thenReturn(kiaSportage2014Dto);

        CarDto result = carService.saveByMakeModelYear("kiaSportage2014", "Kia", "Sportage", 2014);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(kiaSportage2014Dto);

        verify(carRepository).existsByObjectId("kiaSportage2014");
        verify(makeRepository).findByMakeIgnoreCase("Kia");
        verify(modelRepository).findByModelIgnoreCaseAndMake("Sportage", kia);
        verify(carRepository).save(argThat(car ->
                car.getObjectId().equals("kiaSportage2014") && car.getYear() == 2014 && car.getModel().equals(sportage)
        ));
        verify(carMapper).toDto(kiaSportage2014);

        verify(makeRepository, never()).save(any(Make.class));
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    public void update_shouldUpdateAndReturnCarDto_whenAllDataProvided() {

        when(carRepository.findByObjectId("toyotaCamry2022")).thenReturn(Optional.of(toyotaCamry2022));
        when(carRepository.save(toyotaCamry2022)).thenReturn(toyotaCamry2022);
        when(carMapper.toDto(toyotaCamry2022)).thenReturn(toyotaCamry2022Dto);

        CarDto result = carService.update("toyotaCamry2022", toyotaCamry2022Dto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(toyotaCamry2022Dto);

        verify(carRepository).findByObjectId("toyotaCamry2022");
        verify(carRepository).save(any(Car.class));
        verify(carMapper).toDto(toyotaCamry2022);
    }

    @Test
    public void deleteByObjectId_shouldDeleteCar_whenCarExists() {

        carService.deleteByObjectId("toyotaCamry2022");

        verify(carRepository).deleteByObjectId("toyotaCamry2022");
    }
}
