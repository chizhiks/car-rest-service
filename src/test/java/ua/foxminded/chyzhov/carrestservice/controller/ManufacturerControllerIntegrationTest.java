package ua.foxminded.chyzhov.carrestservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.chyzhov.carrestservice.config.TestSecurityConfig;
import ua.foxminded.chyzhov.carrestservice.dto.CarDto;
import ua.foxminded.chyzhov.carrestservice.dto.CategoryDto;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.service.CarService;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManufacturerController.class)
@Import(TestSecurityConfig.class)
public class ManufacturerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MakeService makeService;

    @MockitoBean
    private ModelService modelService;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private CsvImportService csvImportService;

    @Test
    void getAllManufacturers_shouldReturnManufacturersPage_whenManufacturersExist() throws Exception {

        MakeDto make = new MakeDto(1, "Audi");
        Page<MakeDto> page = new PageImpl<>(List.of(make));

        when(makeService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make").value("Audi"));
    }

    @Test
    void createManufacturer_shouldCreateManufacturer_whenAllDataProvided() throws Exception {

        MakeDto make = new MakeDto(2, "BMW");

        when(makeService.save(any(MakeDto.class))).thenReturn(make);

        mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "makeId": 2,
                                  "make": "BMW"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make").value("BMW"));
    }

    @Test
    void getManufacturerById_shouldReturnManufacturerById_whenManufacturerWithIdExists() throws Exception {

        MakeDto make = new MakeDto(3, "Tesla");

        when(makeService.findById(3)).thenReturn(make);

        mockMvc.perform(get("/api/v1/manufacturers/id/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Tesla"));
    }

    @Test
    void getManufacturerByName_shouldReturnManufacturerByName_whenManufacturerWithManufacturerNameExists() throws Exception {

        MakeDto make = new MakeDto(4, "Alfa Romeo");

        when(makeService.findByName("Alfa Romeo")).thenReturn(make);

        mockMvc.perform(get("/api/v1/manufacturers/Alfa Romeo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Alfa Romeo"));
    }

    @Test
    void updateManufacturer_shouldUpdateManufacturer_whenManufacturerExistsAndAllDataProvided() throws Exception {

        MakeDto existingMake = new MakeDto(1, "Tesla");
        MakeDto updatedMake = new MakeDto(1, "Tesla Motors");

        when(makeService.findByName("Tesla")).thenReturn(existingMake);
        when(makeService.update(eq(1), any(MakeDto.class))).thenReturn(updatedMake);

        mockMvc.perform(put("/api/v1/manufacturers/Tesla")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "makeId": 1,
                                  "make": "Tesla Motors"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Tesla Motors"));
    }

    @Test
    void deleteManufacturerByName_shouldDeleteManufacturer_whenManufacturerWithNameExists() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");

        when(makeService.findByName("Tesla")).thenReturn(tesla);
        doNothing().when(makeService).deleteById(1);

        mockMvc.perform(delete("/api/v1/manufacturers/Tesla"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getModelsByManufacturer_shouldReturnModelsPage_whenModelsExist() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, tesla, "Model S");
        ModelDto modelY = new ModelDto(2, tesla, "Model Y");
        ModelDto cybertruck = new ModelDto(3, tesla, "Cybertruck");

        Page<ModelDto> page = new PageImpl<>(List.of(modelS, modelY, cybertruck));

        when(makeService.findByName("Tesla")).thenReturn(tesla);
        when(modelService.findAllByMakeId(eq(1), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/manufacturers/Tesla/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make.make").value("Tesla"))
                .andExpect(jsonPath("$.content[0].model").value("Model S"))
                .andExpect(jsonPath("$.content[1].make.make").value("Tesla"))
                .andExpect(jsonPath("$.content[1].model").value("Model Y"))
                .andExpect(jsonPath("$.content[2].make.make").value("Tesla"))
                .andExpect(jsonPath("$.content[2].model").value("Cybertruck"));
    }

    @Test
    void createModelForManufacturer_shouldCreateModelForManufacturer_whenManufacturerExistsAndModelExists() throws Exception {

        MakeDto make = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, make, "Model S");

        when(modelService.saveByMakeName(any(String.class), any(String.class))).thenReturn(modelS);

        mockMvc.perform(post("/api/v1/manufacturers/Tesla/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "model": "Model S",
                                  "make": {
                                    "makeId": 1,
                                    "make": "Tesla"
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"));
    }

    @Test
    void getModelByManufacturerAndName_shouldReturnModelByManufacturerAndName_whenModelWithModelAndManufacturerExists() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, tesla, "Model S");

        when(modelService.findByMakeNameAndModelName("Tesla", "Model S")).thenReturn(modelS);

        mockMvc.perform(get("/api/v1/manufacturers/Tesla/models/Model S"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"));
    }

    @Test
    void createCarByManufacturerModelYear_shouldCreateManufacturerCorrectly_whenAllDataProvided() throws Exception {

        MakeDto make = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, make, "Model S");
        CategoryDto sedan = new CategoryDto(1, "Sedan");

        CarDto car = new CarDto("111", modelS, 2020, Set.of(sedan));

        when(carService.saveByMakeModelYear(any(String.class), any(String.class), any(String.class), any(Integer.class))).thenReturn(car);

        mockMvc.perform(post("/api/v1/manufacturers/Tesla/models/Model S/2020")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "model": {
                                    "model": "Sedan",
                                    "make": {
                                      "makeId": 1,
                                      "make": "Tesla"
                                    },
                                    "year": 2020
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model.make.make").value("Tesla"))
                .andExpect(jsonPath("$.model.model").value("Model S"));
    }

    @Test
    void updateModelByManufacturerAndName_shouldUpdateModelByManufacturerAndName_whenModelWithModelAndManufacturerExistsAndAllDataProvided() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, tesla, "Model S");

        when(modelService.findByMakeNameAndModelName("Tesla", "Model S")).thenReturn(modelS);
        when(modelService.update(any(Integer.class), any(ModelDto.class))).thenReturn(modelS);

        mockMvc.perform(put("/api/v1/manufacturers/Tesla/models/Model S")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                  "model": "Model S",
                                  "make": {
                                    "makeId": 1,
                                    "make": "Tesla Motors"
                                  }
                                }
"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"));
    }

    @Test
    void deleteModelByManufacturerAndName_shouldDeleteModelByManufacturerAndName_whenModelWithModelAndManufacturerExists() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");
        ModelDto modelS = new ModelDto(1, tesla, "Model S");

        when(modelService.findByMakeNameAndModelName("Tesla", "Model S")).thenReturn(modelS);
        doNothing().when(modelService).deleteById(eq(1));

        mockMvc.perform(delete("/api/v1/manufacturers/Tesla/models/Model S"))
                .andExpect(status().isNoContent());
    }

}
