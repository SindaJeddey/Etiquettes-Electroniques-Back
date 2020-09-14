package project.ee.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.inStoreProduct.InStoreProductDTO;
import project.ee.dto.movement.MovementDTO;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.promotion.PromotionDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.services.MovementService;
import project.ee.services.ProductService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
@PreAuthorize("permitAll()")
//@PreAuthorize("hasAnyAuthority('ROLE_OPERATOR','ROLE_ADMIN')")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    List<ProductDTO> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    ProductDTO getProduct(@PathVariable String id) throws NotFoundException {
        return productService.getProduct(id);
    }

    @PostMapping("/new")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO == null)
            throw new IllegalArgumentException("Must provide a product to save");
        return productService.saveNewProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO)
            throws NotFoundException {
        if (productDTO == null)
            throw new IllegalArgumentException("Must provide a product to update");
        return productService.updateProduct(id,productDTO);
    }

    @PutMapping("/promotions/{id}/add")
    public void addPromotion(@PathVariable String id, @RequestBody PromotionDTO promotionDTO) throws NotFoundException {
        productService.addPromotion(id,promotionDTO);
    }

    @GetMapping("/promotions/{id}")
    public PromotionDTO getCurrentPromo(@PathVariable String id) throws NotFoundException {
        return productService.getCurrentPromotion(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) throws NotFoundException {
        productService.deleteProduct(id);
    }



}
