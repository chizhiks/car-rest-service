package ua.foxminded.chyzhov.carrestservice.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.chyzhov.carrestservice.entity.Car;
import ua.foxminded.chyzhov.carrestservice.entity.Category;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;
import ua.foxminded.chyzhov.carrestservice.repository.CarRepository;
import ua.foxminded.chyzhov.carrestservice.repository.CategoryRepository;
import ua.foxminded.chyzhov.carrestservice.repository.MakeRepository;
import ua.foxminded.chyzhov.carrestservice.repository.ModelRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CsvImportService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MakeRepository makeRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void importCarsFromCsv(String csvFilePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord record : csvParser) {
                processCarRecord(record);
            }
        }
    }

    private void processCarRecord(CSVRecord record) {

        String objectId = record.get("objectId");
        String makeName = record.get("make");
        String modelName = record.get("model");
        Integer year = Integer.valueOf(record.get("year"));
        String categoriesStr = record.get("Category");

        Make make = makeRepository.findByMakeIgnoreCase(makeName)
                .orElseGet(() -> {
                    Make newMake = new Make();
                    newMake.setMake(makeName);
                    return makeRepository.save(newMake);
                });

        Model model = modelRepository.findByModelIgnoreCaseAndMake(modelName, make)
                .orElseGet(() -> {
                    Model modelNew = new Model();
                    modelNew.setModel(modelName);
                    modelNew.setMake(make);
                    return modelRepository.save(modelNew);
                });

        Set<Category> categories = new HashSet<>();
        if (categoriesStr != null && !categoriesStr.trim().isEmpty()) {
            String[] categoryNames = categoriesStr.split(",");
            for (String categoryName : categoryNames) {
                categoryName = categoryName.trim();
                if (!categoryName.isEmpty()) {
                    String finalCategoryName = categoryName;
                    Category category = categoryRepository.findByCategory(categoryName)
                            .orElseGet(() -> {
                                Category newCategory = new Category();
                                newCategory.setCategory(finalCategoryName);
                                return categoryRepository.save(newCategory);
                            });
                    categories.add(category);
                }
            }
        }

        Car car = carRepository.findByObjectId(objectId)
                .orElse(new Car());

        car.setObjectId(objectId);
        car.setModel(model);
        car.setYear(year);
        car.setCategories(categories);

        carRepository.save(car);
    }
}
