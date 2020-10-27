package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.promotion.PromotionDTO;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.services.ProductService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_URI)
@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN','ROLE_SUPER_OPERATOR')")
@Validated
public class ProductController {

    public static final String BASE_URI="/api/products";
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable String id){
        return productService.getProduct(id);
    }

    @PostMapping("/new")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        return productService.saveNewProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable String id, ProductDTO productDTO){
        return productService.updateProduct(id,productDTO);
    }

    @PutMapping("/promotions/{id}/add")
    public void addPromotion(@PathVariable String id, PromotionDTO promotionDTO) {
        productService.addPromotion(id,promotionDTO);
    }

    @GetMapping("/promotions/{id}")
    public PromotionDTO getCurrentPromo(@PathVariable String id){
        return productService.getCurrentPromotion(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
    }



}
