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
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.mapper.MakeMapper;
import ua.foxminded.chyzhov.carrestservice.mapper.ModelMapper;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import ua.foxminded.chyzhov.carrestservice.service.impl.ModelServiceImpl;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MakeMapper makeMapper;

    @InjectMocks
    private ModelServiceImpl modelService;

    Make toyota;
    Make bmw;
    Make volkswagen;

    MakeDto toyotaDto;
    MakeDto bmwDto;
    MakeDto volkswagenDto;

    Model camry;
    Model prado;
    Model x5;
    Model passat;

    ModelDto camryDto;
    ModelDto pradoDto;
    ModelDto x5Dto;
    ModelDto passatDto;

    @BeforeEach
    public void setUp() {

        toyota = new Make(1, "Toyota");
        bmw = new Make(2, "BMW");
        volkswagen = new Make(3, "Volkswagen");

        toyotaDto = new MakeDto(1, "Toyota");
        bmwDto = new MakeDto(2, "BMW");
        volkswagenDto = new MakeDto(3, "Volkswagen");

        camry = new Model(1, toyota, "Camry");
        prado = new Model(2, toyota, "Prado");
        x5 = new Model(3, bmw, "X5");
        passat = new Model(4, volkswagen, "Passat");

        camryDto = new ModelDto(1, toyotaDto, "Camry");
        pradoDto = new ModelDto(2, toyotaDto, "Prado");
        x5Dto = new ModelDto(3, bmwDto, "X5");
        passatDto = new ModelDto(4, volkswagenDto, "Passat");
    }

    @Test
    public void findById_shouldReturnModelDto_whenModelWithIdExists() {

        when(modelRepository.findById(1)).thenReturn(Optional.of(camry));
        when(modelMapper.toDto(camry)).thenReturn(camryDto);

        ModelDto result = modelService.findById(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(camryDto);

        verify(modelRepository).findById(1);
        verify(modelMapper).toDto(camry);
    }

    @Test
    public void findAll_shouldReturnPageOfModelDto_whenModelsExist() {

        Pageable pageable = Pageable.unpaged();

        Page<Model> modelPage = new PageImpl<>(Arrays.asList(camry, prado, x5, passat));

        when(modelRepository.findAll(pageable)).thenReturn(modelPage);
        when(modelMapper.toDto(camry)).thenReturn(camryDto);
        when(modelMapper.toDto(prado)).thenReturn(pradoDto);
        when(modelMapper.toDto(x5)).thenReturn(x5Dto);
        when(modelMapper.toDto(passat)).thenReturn(passatDto);

        Page<ModelDto> result = modelService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result).contains(camryDto, pradoDto, x5Dto, passatDto);

        verify(modelRepository).findAll(pageable);
        verify(modelMapper).toDto(camry);
        verify(modelMapper).toDto(prado);
        verify(modelMapper).toDto(x5);
        verify(modelMapper).toDto(passat);
    }

    @Test
    public void findAllByMakeId_shouldReturnPageOfModelDto_whenModelsWithMakeExist() {

        Pageable pageable = Pageable.unpaged();

        Page<Model> modelPage = new PageImpl<>(Arrays.asList(camry, prado));

        when(modelRepository.findAllByMakeMakeId(1, pageable)).thenReturn(modelPage);
        when(modelMapper.toDto(camry)).thenReturn(camryDto);
        when(modelMapper.toDto(prado)).thenReturn(pradoDto);

        Page<ModelDto> result = modelService.findAllByMakeId(1, pageable);

        assertThat(result).isNotNull();
        assertThat(result).contains(camryDto, pradoDto);

        verify(modelRepository).findAllByMakeMakeId(1, pageable);
        verify(modelMapper).toDto(camry);
        verify(modelMapper).toDto(prado);
    }

    @Test
    public void findByMakeNameAndModelName_shouldReturnModelDto_whenModelWithProvidedDataExists() {

        when(modelRepository.findByModelIgnoreCaseAndMakeMakeIgnoreCase("Passat", "Volkswagen")).thenReturn(Optional.of(passat));
        when(modelMapper.toDto(passat)).thenReturn(passatDto);

        ModelDto result = modelService.findByMakeNameAndModelName("Volkswagen", "Passat");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(passatDto);

        verify(modelRepository).findByModelIgnoreCaseAndMakeMakeIgnoreCase("Passat", "Volkswagen");
        verify(modelMapper).toDto(passat);
    }

    @Test
    public void save_shouldSaveAndReturnModelDto_whenAllDataProvided() {

        when(modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase("Prado", "Toyota")).thenReturn(Boolean.FALSE);
        when(modelMapper.toEntity(pradoDto)).thenReturn(prado);
        when(modelRepository.save(prado)).thenReturn(prado);
        when(modelMapper.toDto(prado)).thenReturn(pradoDto);

        ModelDto result = modelService.save(pradoDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(pradoDto);

        verify(modelRepository).existsByModelIgnoreCaseAndMakeMakeIgnoreCase("Prado", "Toyota");
        verify(modelMapper).toEntity(pradoDto);
        verify(modelRepository).save(prado);
        verify(modelMapper).toDto(prado);
    }

    @Test
    public void saveByMakeName_shouldSaveAndReturnModelDto_whenAllDataProvided() {

        when(makeRepository.findByMakeIgnoreCase("BMW")).thenReturn(Optional.of(bmw));
        when(modelRepository.existsByModelIgnoreCaseAndMakeMakeIgnoreCase("X5", "BMW")).thenReturn(Boolean.FALSE);
        when(modelRepository.save(any(Model.class))).thenReturn(x5);
        when(modelMapper.toDto(x5)).thenReturn(x5Dto);

        ModelDto result = modelService.saveByMakeName("BMW", "X5");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(x5Dto);

        verify(makeRepository).findByMakeIgnoreCase("BMW");
        verify(modelRepository).existsByModelIgnoreCaseAndMakeMakeIgnoreCase("X5", "BMW");
        verify(modelRepository).save(any(Model.class));
        verify(modelMapper).toDto(x5);
    }

    @Test
    public void update_shouldUpdateAndReturnModelDto_whenAllDataProvided() {

        when(modelRepository.findById(2)).thenReturn(Optional.of(prado));
        when(makeMapper.toEntity(any(MakeDto.class))).thenReturn(prado.getMake());
        when(modelRepository.save(prado)).thenReturn(prado);
        when(modelMapper.toDto(prado)).thenReturn(pradoDto);

        ModelDto result = modelService.update(2, pradoDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(pradoDto);

        verify(modelRepository).findById(2);
        verify(makeMapper).toEntity(any(MakeDto.class));
        verify(modelRepository).save(prado);
        verify(modelMapper).toDto(prado);
    }

    @Test
    public void deleteById_shouldDeleteModel_whenModelWithIdExists() {

        modelService.deleteById(3);

        verify(modelRepository).deleteById(3);
    }
}
