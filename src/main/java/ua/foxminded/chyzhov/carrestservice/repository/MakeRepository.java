package ua.foxminded.chyzhov.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.chyzhov.carrestservice.entity.Make;

import java.util.Optional;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer> {

    boolean existsByMake(String make);

    Optional<Make> findByMakeIgnoreCase(String make);
}
