package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.InStoreProduct;

import java.util.Optional;

public interface InStoreProductRepository extends JpaRepository<InStoreProduct,Long> {
    Optional<InStoreProduct> findByInStoreProductCode(String code);
}
