package project.EE.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import project.EE.dto.category.CategoryDTO;
import project.EE.dto.product.ProductDTO;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    private Long id;
    private String name;
    private String location;
    private String zipCode;
    private Set<ProductDTO> products;
    private Set<CategoryDTO> categories;
}
