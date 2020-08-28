package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.InStoreProduct;

public interface InStoreProductRepository extends JpaRepository<InStoreProduct,Long> {
}
