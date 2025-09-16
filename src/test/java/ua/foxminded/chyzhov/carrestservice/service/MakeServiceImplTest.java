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
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.mapper.MakeMapper;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.service.impl.MakeServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MakeServiceImplTest {

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private MakeMapper makeMapper;

    @InjectMocks
    private MakeServiceImpl makeService;

    Make toyota;
    Make bmw;
    Make volkswagen;

    MakeDto toyotaDto;
    MakeDto bmwDto;
    MakeDto volkswagenDto;

    @BeforeEach
    public void setUp() {

        toyota = new Make(1, "Toyota");
        bmw = new Make(2, "BMW");
        volkswagen = new Make(3, "Volkswagen");

        toyotaDto = new MakeDto(1, "Toyota");
        bmwDto = new MakeDto(2, "BMW");
        volkswagenDto = new MakeDto(3, "Volkswagen");
    }

    @Test
    public void findById_shouldReturnMakeDto_whenMakeWithIdExists() {

        when(makeRepository.findById(1)).thenReturn(Optional.of(toyota));
        when(makeMapper.toDto(toyota)).thenReturn(toyotaDto);

        MakeDto result = makeService.findById(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(toyotaDto);

        verify(makeRepository).findById(1);
        verify(makeMapper).toDto(toyota);
    }

    @Test
    public void findByName_shouldReturnMakeDto_whenMakeWithNameExists() {

        when(makeRepository.findByMakeIgnoreCase("BMW")).thenReturn(Optional.of(bmw));
        when(makeMapper.toDto(bmw)).thenReturn(bmwDto);

        MakeDto result = makeService.findByName("BMW");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(bmwDto);

        verify(makeRepository).findByMakeIgnoreCase("BMW");
        verify(makeMapper).toDto(bmw);
    }

    @Test
    public void findAll_shouldReturnPageOfMakeDto_whenMakesExist() {

        Pageable pageable = Pageable.unpaged();
        Page<Make> makePage = new PageImpl<>(Arrays.asList(toyota,  bmw, volkswagen));

        when(makeRepository.findAll(pageable)).thenReturn(makePage);
        when(makeMapper.toDto(toyota)).thenReturn(toyotaDto);
        when(makeMapper.toDto(bmw)).thenReturn(bmwDto);
        when(makeMapper.toDto(volkswagen)).thenReturn(volkswagenDto);

        Page<MakeDto> result = makeService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result).contains(toyotaDto, bmwDto, volkswagenDto);

        verify(makeRepository).findAll(pageable);
        verify(makeMapper).toDto(toyota);
        verify(makeMapper).toDto(bmw);
        verify(makeMapper).toDto(volkswagen);
    }

    @Test
    public void save_shouldSaveAndReturnMakeDto_whenAllDataProvided() {

        when(makeRepository.existsByMake(volkswagen.getMake())).thenReturn(false);
        when(makeMapper.toEntity(volkswagenDto)).thenReturn(volkswagen);
        when(makeRepository.save(volkswagen)).thenReturn(volkswagen);
        when(makeMapper.toDto(volkswagen)).thenReturn(volkswagenDto);

        MakeDto result = makeService.save(volkswagenDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(volkswagenDto);

        verify(makeRepository).existsByMake(volkswagen.getMake());
        verify(makeMapper).toEntity(volkswagenDto);
        verify(makeRepository).save(volkswagen);
        verify(makeMapper).toDto(volkswagen);
    }

    @Test
    public void update_shouldUpdateAndReturnMakeDto_whenAllDataProvided() {

        when(makeRepository.findById(1)).thenReturn(Optional.of(toyota));
        when(makeRepository.save(toyota)).thenReturn(toyota);
        when(makeMapper.toDto(toyota)).thenReturn(toyotaDto);

        MakeDto result = makeService.update(1, toyotaDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(toyotaDto);

        verify(makeRepository).findById(1);
        verify(makeRepository).save(toyota);
        verify(makeMapper).toDto(toyota);
    }

    @Test
    public void deleteById_shouldDeleteMake_whenMakeExists() {

        makeService.deleteById(1);

        verify(makeRepository).deleteById(1);
    }
}
