package ua.foxminded.chyzhov.carrestservice.controller;

import org.junit.jupiter.api.BeforeEach;
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
import ua.foxminded.chyzhov.carrestservice.dto.*;
import ua.foxminded.chyzhov.carrestservice.service.CarService;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
@Import(TestSecurityConfig.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CsvImportService csvImportService;

    @MockitoBean
    private CarService carService;

    private MakeDto audi;
    private MakeDto bmw;
    private MakeDto volkswagen;

    private CategoryDto sedan;
    private CategoryDto suv;

    private ModelDto rsq8;
    private ModelDto x5;
    private ModelDto passat;

    private CarDto audiRSQ8;
    private CarDto bmwX5;
    private CarDto volkswagenPassat;

    @BeforeEach
    void setUp() {

        audi = new MakeDto(1, "Audi");
        bmw = new MakeDto(2, "BMW");
        volkswagen = new MakeDto(3, "Volkswagen");

        sedan = new CategoryDto(1, "Sedan");
        suv = new CategoryDto(2, "Suv");

        rsq8 = new ModelDto(1, audi, "RSQ8");
        x5 = new ModelDto(2, bmw, "X5");
        passat = new ModelDto(3, volkswagen, "Passat");

        audiRSQ8 = new CarDto("121audirsq8", rsq8, 2022, Set.of(suv));
        bmwX5 = new CarDto("222bmwx5", x5, 2015, Set.of(suv));
        volkswagenPassat = new CarDto("313volkswagenpassat", passat, 2017, Set.of(sedan));
    }

    @Test
    void getAllCars_shouldReturnCarsPage_whenCarsExist() throws Exception {
        Page<CarDto> page = new PageImpl<>(List.of(audiRSQ8, bmwX5, volkswagenPassat));

        when(carService.getFilteredCars(any(CarFilterDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].model.make.make").value("Audi"))
                .andExpect(jsonPath("$.content[0].model.model").value("RSQ8"));
    }

    @Test
    void createCar_shouldCreateCar_whenAllDataProvided() throws Exception {

        MakeDto makeFord = new MakeDto(4, "Ford");
        ModelDto modelF150 = new ModelDto(4, makeFord, "F150");
        CategoryDto truck = new CategoryDto(3, "Truck");


        CarDto fordF150 = new CarDto("443fordf150", modelF150, 2010, Set.of(truck));

        when(carService.save(any(CarDto.class))).thenReturn(fordF150);

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "objectId": "443fordf150",
                                  "model": {
                                    "modelId": 4,
                                    "make": {
                                      "makeId": 4,
                                      "make": "Ford"
                                    },
                                    "model": "F150"
                                  },
                                  "year": 2010,
                                  "categories": [
                                    {
                                      "categoryId": 3,
                                      "category": "Truck"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model.make.make").value("Ford"))
                .andExpect(jsonPath("$.model.model").value("F150"));
    }

    @Test
    void getCarByObjectId_shouldReturnCarByObjectId_whenCarWithObjectIdExists() throws Exception {

        when(carService.findByObjectId("222bmwx5")).thenReturn(bmwX5);

        mockMvc.perform(get("/api/v1/cars/222bmwx5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.make.make").value("BMW"))
                .andExpect(jsonPath("$.model.model").value("X5"));
    }

    @Test
    void updateCar_shouldUpdateCar_whenCarExistsAndAllDataProvided() throws Exception {

        MakeDto tesla = new MakeDto(5, "Tesla");

        ModelDto model3 = new ModelDto(5, tesla, "Model 3");

        CarDto teslaModel3 = new CarDto("515teslamodel3", model3, 2020, Set.of(sedan));

        when(carService.update(any(String.class), any(CarDto.class))).thenReturn(teslaModel3);

        mockMvc.perform(put("/api/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "objectId": "515teslamodel3",
                                  "model": {
                                    "modelId": 5,
                                    "make": {
                                      "makeId": 5,
                                      "make": "Tesla"
                                    },
                                    "model": "Model 3"
                                  },
                                  "year": 2020,
                                  "categories": [
                                    {
                                      "categoryId": 1,
                                      "category": "Sedan"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.make.make").value("Tesla"))
                .andExpect(jsonPath("$.model.model").value("Model 3"));
    }

    @Test
    void deleteCarByObjectId_shouldDeleteCar_whenCarWithObjectIdExists() throws Exception {
        doNothing().when(carService).deleteByObjectId("515teslamodel3");

        mockMvc.perform(delete("/api/v1/cars/515teslamodel3"))
                .andExpect(status().isNoContent());
    }
}
