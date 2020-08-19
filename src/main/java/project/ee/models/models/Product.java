package project.ee.models.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    private Category category;

    @ManyToMany(mappedBy = "products")
    private Set<Store> stores = new HashSet<>();

}
