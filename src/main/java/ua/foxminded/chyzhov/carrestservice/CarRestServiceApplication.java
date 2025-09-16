package ua.foxminded.chyzhov.carrestservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

@Slf4j
@SpringBootApplication
public class CarRestServiceApplication implements CommandLineRunner {

    @Autowired
    private CsvImportService csvImportService;

    public static void main(String[] args) {

        SpringApplication.run(CarRestServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String csvFilePath = "src/main/resources/cars.csv";

        log.info("Starting to import data from CSV...");
        csvImportService.importCarsFromCsv(csvFilePath);
        log.info("Import completed successfully!");
    }
}
