package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Movement;

import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement,Long> {
    Optional<Movement> findByMovementCode(String code);
}
