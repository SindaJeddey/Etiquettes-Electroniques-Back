package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.models.Store;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
