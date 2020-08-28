package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Movement;

public interface MovementRepository extends JpaRepository<Movement,Long> {
}
