package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {
}
