package project.ee.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ee.dto.product.ProductDTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private String categoryCode;

    @NotEmpty(message = "Must provide a category name")
    private String name;

    private Set<ProductDTO> products;
}
