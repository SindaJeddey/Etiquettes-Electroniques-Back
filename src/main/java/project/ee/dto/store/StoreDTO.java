package project.ee.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import project.ee.dto.inStoreProduct.InStoreProductDTO;

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
