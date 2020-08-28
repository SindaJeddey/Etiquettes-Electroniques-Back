package project.ee.dto.tag;

import lombok.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;
    private String type;
    private String code;
    private String name;
    private InStoreProductDTO product;
    private Long transmitterId;
}
