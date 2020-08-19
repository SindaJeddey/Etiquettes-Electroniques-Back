package project.ee.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private Long quantity;
    private Long quantityThreshold;
    private LocalDate addedDate;
    private LocalDate lastModificationDate;
    private String unity;
    private String devise;
    private String promotion;
    private String promotionType;
    private String longDescription;
    private String shortDescription;
    private String productCode;
    private byte[] image1;
    private byte[] image2;
    private byte[] image3;
    private byte[] barcode;
    private Long categoryId;

}
