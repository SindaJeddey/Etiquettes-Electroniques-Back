package project.ee.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.store.StoreDTO;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private Set<ProductDTO> productSet;
    private Set<StoreDTO> storeSet;

}
