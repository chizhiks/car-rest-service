package ua.foxminded.chyzhov.carrestservice.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.foxminded.chyzhov.carrestservice.dto.CarFilterDto;
import ua.foxminded.chyzhov.carrestservice.entity.*;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarSpecificationBuilder {

    public static final String WILDCARD = "%";

    public Specification<Car> build(CarFilterDto filterDto) {
        return (root, query, cb) -> {
            List<Predicate> criteriaPredicates = new ArrayList<>();

            // Make Filter
            if (filterDto.manufacturer() != null && !filterDto.manufacturer().trim().isEmpty()) {
                Join<Car, Model> modelJoin = root.join(Car_.model);
                Join<Model, Make> makeJoin = modelJoin.join(Model_.make);
                criteriaPredicates.add(
                        cb.like(cb.lower(makeJoin.get(Make_.make)),
                                WILDCARD + filterDto.manufacturer().toLowerCase() + WILDCARD)
                );
            }

            // Model Filter
            if (filterDto.model() != null && !filterDto.model().trim().isEmpty()) {
                Join<Car, Model> modelJoin = root.join(Car_.model);
                criteriaPredicates.add(
                        cb.like(cb.lower(modelJoin.get(Model_.model)),
                                WILDCARD + filterDto.model().toLowerCase() + WILDCARD)
                );
            }

            // Category Filter
            if (filterDto.category() != null && !filterDto.category().trim().isEmpty()) {
                Join<Car, Category> categoriesJoin = root.join(Car_.categories);
                criteriaPredicates.add(
                        cb.like(cb.lower(categoriesJoin.get(Category_.category)),
                                WILDCARD + filterDto.category().toLowerCase() + WILDCARD)
                );
            }

            // Year range Filters
            if (filterDto.minYear() != null) {
                criteriaPredicates.add(cb.greaterThanOrEqualTo(root.get(Car_.year), filterDto.minYear()));
            }

            if (filterDto.maxYear() != null) {
                criteriaPredicates.add(cb.lessThanOrEqualTo(root.get(Car_.year), filterDto.maxYear()));
            }

            return cb.and(criteriaPredicates.toArray(new Predicate[0]));
        };
    }
}
