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
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MakeController.class)
@Import(TestSecurityConfig.class)
class MakeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MakeService makeService;

    @MockitoBean
    private CsvImportService csvImportService;

    @Test
    void getAllMakes_shouldReturnMakesPage_whenMakesExist() throws Exception {

        MakeDto make = new MakeDto(1, "Audi");
        Page<MakeDto> page = new PageImpl<>(List.of(make));

        when(makeService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/makes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make").value("Audi"));
    }

    @Test
    void createMake_shouldCreateMake_whenAllDataProvided() throws Exception {

        MakeDto make = new MakeDto(2, "BMW");

        when(makeService.save(any(MakeDto.class))).thenReturn(make);

        mockMvc.perform(post("/api/v1/makes")
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
    void getMakeById_shouldReturnMakeById_whenMakeWithIdExists() throws Exception {

        MakeDto make = new MakeDto(3, "Tesla");

        when(makeService.findById(3)).thenReturn(make);

        mockMvc.perform(get("/api/v1/makes/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Tesla"));
    }

    @Test
    void getMakeByName_shouldReturnMakeByName_whenMakeWithMakeNameExists() throws Exception {

        MakeDto make = new MakeDto(4, "Alfa Romeo");

        when(makeService.findByName("Alfa Romeo")).thenReturn(make);

        mockMvc.perform(get("/api/v1/makes/name/Alfa Romeo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Alfa Romeo"));
    }

    @Test
    void updateMake_shouldUpdateMake_whenMakeExistsAndAllDataProvided() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");

        when(makeService.update(any(Integer.class), any(MakeDto.class))).thenReturn(tesla);

        mockMvc.perform(put("/api/v1/makes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "makeId": 1,
                                  "make": "Tesla"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Tesla"));
    }

    @Test
    void deleteMakeById_shouldDeleteMake_whenMakeWithIdExists() throws Exception {

        MakeDto tesla = new MakeDto(1, "Tesla");

        doNothing().when(makeService).deleteById(1);

        mockMvc.perform(delete("/api/v1/makes/1"))
                .andExpect(status().isNoContent());
    }
}
