package ua.foxminded.chyzhov.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.foxminded.chyzhov.carrestservice.entity.Car;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String>, JpaSpecificationExecutor<Car> {

    Optional<Car> findByObjectId(String objectId);

    boolean existsByObjectId(String objectId);

    void deleteByObjectId(String objectId);
}
