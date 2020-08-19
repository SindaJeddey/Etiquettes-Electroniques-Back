package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}