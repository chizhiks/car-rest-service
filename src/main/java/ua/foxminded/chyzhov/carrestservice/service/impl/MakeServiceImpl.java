package ua.foxminded.chyzhov.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.mapper.CarMapper;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.NotFoundException;
import ua.foxminded.chyzhov.carrestservice.util.exceptions.RecordAlreadyExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeServiceImpl implements MakeService {

    private final MakeRepository makeRepository;
    private final CarMapper carMapper;

    @Override
    public MakeDto findById(Integer makeId) {

        Make make = makeRepository.findById(makeId).orElseThrow(() -> new NotFoundException("Make", makeId));

        log.info("Make with makeId: {} found successfully", makeId);

        return carMapper.toDto(make);
    }

    @Override
    public MakeDto findByName(String name) {

        Make make = makeRepository.findByMakeIgnoreCase(name).orElseThrow(() -> new NotFoundException("Make", name));

        log.info("Make with name: {} found successfully", name);

        return carMapper.toDto(make);
    }

    @Override
    public Page<MakeDto> findAll(Pageable pageable) {

        Page<Make> makePage = makeRepository.findAll(pageable);

        log.info("{} makes have been retrieved successfully", makePage.getTotalElements());

        return makePage.map(carMapper::toDto);
    }

    @Override
    @Transactional
    public MakeDto save(MakeDto makeDto) {

        if (makeRepository.existsByMake(makeDto.make())) {
            log.error("Make with makeName: {} already exists", makeDto.make());
            throw new RecordAlreadyExists("Make", "makeName", makeDto.make());
        }

        Make make = carMapper.toEntity(makeDto);

        Make savedMake = makeRepository.save(make);

        log.info("Make with makeId: {} was successfully saved", savedMake.getMakeId());

        return carMapper.toDto(savedMake);
    }

    @Override
    @Transactional
    public MakeDto update(Integer makeId, MakeDto makeDto) {

        Make make = makeRepository.findById(makeId).orElseThrow(() -> new NotFoundException("Make", makeId));

        make.setMake(makeDto.make());

        Make updatedMake = makeRepository.save(make);

        log.info("Make with makeId: {} was successfully updated", updatedMake.getMakeId());

        return carMapper.toDto(updatedMake);
    }

    @Override
    @Transactional
    public void deleteById(Integer makeId) {
        try {
            makeRepository.deleteById(makeId);
            log.info("Make with makeId: {} was successfully deleted", makeId);
        } catch (Exception e) {
            log.error("Error deleting make with makeId: {}", makeId, e);
            throw new NotFoundException("Make", makeId);
        }
    }
}
