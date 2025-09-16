package ua.foxminded.chyzhov.carrestservice.service;

import org.springframework.data.domain.Page;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CarFilterDto;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {

    CarDto findByObjectId(String objectId);

    Page<CarDto> getFilteredCars(CarFilterDto filterDto, Pageable pageable);

    List<CarDto> findAll();

    CarDto save(CarDto carDto);

    CarDto saveByMakeModelYear(String objectId, String make, String model, Integer year);

    CarDto update(String objectId, CarDto carDto);

    void deleteByObjectId(String objectId);
}
