package ua.foxminded.chyzhov.carrestservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;

public interface MakeService {

    MakeDto findById(Integer makeId);

    MakeDto findByName(String name);

    Page<MakeDto> findAll(Pageable pageable);

    MakeDto save(MakeDto makeDto);

    MakeDto update(Integer makeId, MakeDto makeDto);

    void deleteById(Integer makeId);
}
