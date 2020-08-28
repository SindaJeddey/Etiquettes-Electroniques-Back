package project.ee.dto.movement;

import lombok.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.product.ProductDTO;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementDTO {

    private Long id;

    private String type;

    private LocalDate movementDate;

    private Long quantity;

    private InStoreProductDTO product;
}
