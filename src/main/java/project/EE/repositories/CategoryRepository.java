package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
