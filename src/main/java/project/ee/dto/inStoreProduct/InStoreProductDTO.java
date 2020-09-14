package project.ee.dto.inStoreProduct;

import lombok.*;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.store.StoreDTO;
import project.ee.dto.tag.TagDTO;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InStoreProductDTO {

    private ProductDTO product;
    private StoreDTO store;
    private TagDTO tag;
    private String inStoreProductCode;
    private Set<MovementDTO> movements = new HashSet<>();

}
