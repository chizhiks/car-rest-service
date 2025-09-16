package ua.foxminded.chyzhov.carrestservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.dto.ModelDto;
import ua.foxminded.chyzhov.carrestservice.service.ModelService;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModelController.class)
public class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModelService modelService;

    @MockitoBean
    private CsvImportService csvImportService;

    private MakeDto audi;
    private MakeDto bmw;
    private MakeDto volkswagen;

    @BeforeEach
    void setUp() {

        audi = new MakeDto(1, "Audi");
        bmw = new MakeDto(2, "BMW");
        volkswagen = new MakeDto(3, "Volkswagen");
    }

    @Test
    void getAllModels_shouldReturnModelsPage_whenModelsExist() throws Exception {

        ModelDto model = new ModelDto(1, audi, "A4");
        Page<ModelDto> page = new PageImpl<>(List.of(model));

        when(modelService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make.make").value("Audi"))
                .andExpect(jsonPath("$.content[0].model").value("A4"));
    }

    @Test
    void createModel_shouldCreateModel_whenAllDataProvided() throws Exception {

        ModelDto model = new ModelDto(2, bmw, "X5");

        when(modelService.save(any(ModelDto.class))).thenReturn(model);

        mockMvc.perform(post("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "modelId": 2,
                                  "make": {
                                    "makeId": 2,
                                    "make": "BMW"
                                  },
                                  "model": "X5"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make.make").value("BMW"))
                .andExpect(jsonPath("$.model").value("X5"));
    }

    @Test
    void getModelById_shouldReturnModelById_whenModelWithIdExists() throws Exception {

        ModelDto model = new ModelDto(3, volkswagen, "Passat");

        when(modelService.findById(3)).thenReturn(model);

        mockMvc.perform(get("/api/v1/models/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make.make").value("Volkswagen"))
                .andExpect(jsonPath("$.model").value("Passat"));
    }

    @Test
    void updateModel_shouldUpdateModel_whenModelExistsAndAllDataProvided() throws Exception {

        ModelDto model = new ModelDto(1, audi, "Q7");

        when(modelService.update(any(Integer.class), any(ModelDto.class))).thenReturn(model);

        mockMvc.perform(put("/api/v1/models/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "modelId": 1,
                                  "make": {
                                    "makeId": 1,
                                    "make": "Audi"
                                  },
                                  "model": "Q7"
                                }
"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make.make").value("Audi"))
                .andExpect(jsonPath("$.model").value("Q7"));
    }

    @Test
    void deleteModelById_shouldDeleteModel_whenModelWithIdExists() throws Exception {

        ModelDto model = new ModelDto(1, bmw, "X7");

        doNothing().when(modelService).deleteById(1);

        mockMvc.perform(delete("/api/v1/models/1"))
                .andExpect(status().isNoContent());
    }

}
