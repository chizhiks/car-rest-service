package ua.foxminded.chyzhov.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CarFilterDto;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.mapper.CarMapper;
import ua.foxminded.chyzhov.carrestservice.repository.CarRepository;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;
import ua.foxminded.chyzhov.carrestservice.service.CarService;

import org.springframework.data.domain.Pageable;
import ua.foxminded.chyzhov.carrestservice.specification.CarSpecificationBuilder;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.NotFoundException;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.RecordAlreadyExists;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarSpecificationBuilder specificationBuilder;
    private final MakeRepository makeRepository;
    private final ModelRepository modelRepository;

    @Override
    public Page<CarDto> getFilteredCars(CarFilterDto filterDto, Pageable pageable) {

        Specification<Car> specification = specificationBuilder.build(filterDto);
        Page<Car> cars = carRepository.findAll(specification, pageable);

        log.info("Filtered cars retrieved successfully with filters: {} and page request: {}", filterDto, pageable);

        return cars.map(carMapper::toDto);
    }

    @Override
    public CarDto findByObjectId(String objectId) {

        Car car = carRepository.findByObjectId(objectId).orElseThrow(() -> new NotFoundException("Car", objectId));

        log.info("Car with objectId: {} found successfully", objectId);

        return carMapper.toDto(car);
    }

    @Override
    public List<CarDto> findAll() {

        List<Car> cars = carRepository.findAll();

        log.info("{} cars have been retrieved successfully", cars.size());

        return cars.stream().map(carMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarDto save(CarDto carDto) {

        if (carRepository.existsByObjectId(carDto.objectId())) {
            log.error("Car with objectId: {} already exists", carDto.objectId());
            throw new RecordAlreadyExists("Car", "objectId", carDto.objectId());
        }

        Car car = carMapper.toEntity(carDto);

        Car savedCar = carRepository.save(car);

        log.info("Car with objectId: {} was successfully saved", savedCar.getObjectId());

        return carMapper.toDto(savedCar);
    }

    @Override
    @Transactional
    public CarDto saveByMakeModelYear(String objectId, String makeName, String modelName, Integer year) {

        if (carRepository.existsByObjectId(objectId)) {
            log.error("Car with objectId: {} already exists", objectId);
            throw new RecordAlreadyExists("Car", "objectId", objectId);
        }

        Make make = makeRepository.findByMakeIgnoreCase(makeName)
                .orElseGet(() -> {
                    Make newMake = new Make();
                    newMake.setMake(makeName);
                    return makeRepository.save(newMake);
                });

        Model model = modelRepository.findByModelIgnoreCaseAndMake(modelName, make)
                .orElseGet(() -> {
                    Model modelNew = new Model();
                    modelNew.setModel(modelName);
                    modelNew.setMake(make);
                    return modelRepository.save(modelNew);
                });

        Car car = new Car();
        car.setObjectId(objectId);
        car.setModel(model);
        car.setYear(year);
        car.setCategories(new HashSet<>());

        Car savedCar = carRepository.save(car);
        log.info("Car with objectId: {} was successfully saved", savedCar.getObjectId());

        return carMapper.toDto(savedCar);
    }

    @Override
    @Transactional
    public CarDto update(String objectId, CarDto carDto) {

        Car car = carRepository.findByObjectId(objectId).orElseThrow(() -> new NotFoundException("Car", carDto.objectId()));

        car.setObjectId(carDto.objectId());
        car.setYear(carDto.year());
        car.setModel(carMapper.toEntity(carDto.model()));
        car.setCategories(carMapper.toEntity(carDto.categories()));

        Car updatedCar = carRepository.save(car);

        log.info("Car with objectId: {} was successfully updated", updatedCar.getObjectId());

        return carMapper.toDto(updatedCar);
    }

    @Override
    @Transactional
    public void deleteByObjectId(String objectId) {
        try {
            carRepository.deleteByObjectId(objectId);
            log.info("Car with objectId: {} was successfully deleted", objectId);
        } catch (Exception e) {
            log.error("Error deleting car with objectId: {}", objectId, e);
            throw new NotFoundException("Car", objectId);
        }
    }
}
