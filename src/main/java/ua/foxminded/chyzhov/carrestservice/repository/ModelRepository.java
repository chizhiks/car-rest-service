package ua.foxminded.chyzhov.carrestservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.chyzhov.carrestservice.entity.Make;
import ua.foxminded.chyzhov.carrestservice.entity.Model;

import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

    boolean existsByModelIgnoreCaseAndMakeMakeIgnoreCase(String model, String make);

    Optional<Model> findByModelIgnoreCaseAndMake(String model, Make make);

    Optional<Model> findByModelIgnoreCaseAndMakeMakeIgnoreCase(String modelName, String makeName);

    Page<Model> findAllByMakeMakeId(Integer makeMakeId, Pageable pageable);
}
