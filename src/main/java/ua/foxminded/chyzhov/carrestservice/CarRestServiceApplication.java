package ua.foxminded.chyzhov.carrestservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@SpringBootApplication
public class CarRestServiceApplication implements CommandLineRunner {

    @Autowired
    private CsvImportService csvImportService;

    @Value("${csv.import.enabled:false}")
    private boolean csvImportEnabled;

    public static void main(String[] args) {

        SpringApplication.run(CarRestServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (!csvImportEnabled) {
            log.info("CSV import is disabled (csv.import.enabled=false)");
            return;
        }

        Path tempFile = null;

        try {
            log.info("Starting to import data from CSV...");

            ClassPathResource csvResource = new ClassPathResource("cars.csv");
            tempFile = Files.createTempFile("cars", ".csv");

            try (InputStream inputStream = csvResource.getInputStream()) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            csvImportService.importCarsFromCsv(tempFile.toString());
            log.info("Import completed successfully!");
        } catch (Exception e) {
            log.error("Failed to import CSV data: {}", e.getMessage(), e);
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (Exception e) {
                    log.error("Failed to delete temp file: {}", e.getMessage(), e);
                }
            }
        }

    }
}
