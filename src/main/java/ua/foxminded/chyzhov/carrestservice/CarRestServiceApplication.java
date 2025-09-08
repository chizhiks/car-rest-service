package ua.foxminded.chyzhov.carrestservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.chyzhov.carrestservice.service.impl.CsvImportService;

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

        System.out.println("Початок імпорту даних з CSV...");
        csvImportService.importCarsFromCsv(csvFilePath);
        System.out.println("Імпорт завершено успішно!");
    }
}
