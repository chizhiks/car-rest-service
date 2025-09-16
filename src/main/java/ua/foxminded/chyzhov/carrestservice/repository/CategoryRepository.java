package ua.foxminded.chyzhov.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.chyzhov.carrestservice.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByCategory(String category);

    Optional<Category> findByCategory(String category);
}
