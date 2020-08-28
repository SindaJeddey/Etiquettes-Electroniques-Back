package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Transmitter;

public interface TransmitterRepository extends JpaRepository<Transmitter,Long> {
}
