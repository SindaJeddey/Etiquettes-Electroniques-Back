package project.ee.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import project.ee.dto.category.CategoryDTO;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.product.ProductDTO;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

    private String name;
    private String location;
    private String zipCode;
    private String storeCode;
    private Set<InStoreProductDTO> inStoreProducts;
}
