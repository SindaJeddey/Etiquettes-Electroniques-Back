package project.EE.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.product.ProductDTO;
import project.EE.exceptions.NotFoundException;
import project.EE.models.Product;
import project.EE.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@PreAuthorize("permitAll()")
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
    ProductDTO getProduct(@PathVariable Long id) throws NotFoundException {
        return productService.getProduct(id);
    }

    @PostMapping("/new")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO)
            throws NotFoundException {
        if (productDTO == null)
            throw new IllegalArgumentException("Must provide a product to save");
        return productService.save(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO)
            throws NotFoundException {
        if (productDTO == null)
            throw new IllegalArgumentException("Must provide a product to update");
        return productService.updateProduct(id,productDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) throws NotFoundException {
        productService.deleteProduct(id);
    }
}
