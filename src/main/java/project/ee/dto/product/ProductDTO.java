package project.ee.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ee.dto.inStoreProduct.InStoreProductDTO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class   ProductDTO {

    private String name;
    private Long quantity;
    private Long quantityThreshold;
    private LocalDate addedDate;
    private LocalDate lastModificationDate;
    private String unity;
    private String devise;
    private String longDescription;
    private String shortDescription;
    private String productCode;
    private String image1;
    private String image2;
    private String  image3;
    private byte[] barcode;
    private String category;
    private Set<InStoreProductDTO> inStoreProductDTOSet = new HashSet<>();

}
