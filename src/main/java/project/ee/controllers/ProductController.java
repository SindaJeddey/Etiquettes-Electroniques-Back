package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.promotion.PromotionDTO;
import project.ee.services.ProductService;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_URI)
@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN','ROLE_SUPER_OPERATOR')")
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

    @PostMapping()
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        return productService.saveNewProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO){
        return productService.updateProduct(id,productDTO);
    }

    @PutMapping("/promotions/{id}/add")
    public void addPromotion(@PathVariable String id, @RequestBody PromotionDTO promotionDTO) {
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
