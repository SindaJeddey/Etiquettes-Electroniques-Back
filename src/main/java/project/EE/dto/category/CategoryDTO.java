package project.EE.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.store.StoreDTO;

import java.util.HashSet;
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
