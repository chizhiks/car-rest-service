package ua.foxminded.chyzhov.carrestservice.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public CsvImportService csvImportService() {
        return Mockito.mock(CsvImportService.class);
    }
}
