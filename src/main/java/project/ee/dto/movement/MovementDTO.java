package project.ee.dto.movement;

import lombok.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementDTO {

    private String type;

    private LocalDate movementDate;

    private Long quantity;

    private InStoreProductDTO product;

    private String movementCode;
}
